package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.HttpUrl;

/**
 * Main Activity controller. Contains a list of items from a RSS feed
 */
public class MainActivity extends AppCompatActivity {
    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;
    private ActionBar mActionBar;
    private ListView mListView;
    private int limit;
    private int refresh;
    private HttpUrl url;
    private List<FeedItem> feedItems;
    private Handler uiHandler;
    private boolean run = true;

    private static final int DEFAULT_LIMIT = 15;
    private static final int DEFAULT_REFRESH = 30;
    private static final int MINUTES_TO_MS_CONVERSION = 60000;

    /**
     * Called when the activity is launched.
     * @param savedInstanceState Bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiHandler = new Handler();

        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);

        mListView = findViewById(R.id.listViewFeed);

        getDataFromPreferences();
        createUpdater();

        try {
            fetchUpdate();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Fetch preferences, in this activity limit and refresh is the only relevant
     */
    private void getDataFromPreferences() {
        final SharedPreferences preferences = getSharedPreferences(PreferencesActivity.PREFS_NAME, Context.MODE_PRIVATE);
        this.limit = preferences.getInt(PreferencesActivity.PREFS_LIMIT, DEFAULT_LIMIT);
        this.refresh = preferences.getInt(PreferencesActivity.PREFS_REFRESH, DEFAULT_REFRESH);
    }

    /**
     * https://stackoverflow.com/a/23356082/7036624
     * Creates a TimerTask which counts down from refresh. When refresh is
     * counted down, trigger fetchUpdate()
     */
    private void createUpdater(){
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    public void run() {
                        try {
                            fetchUpdate();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, refresh); //execute in every 10 minutes
    }

    /**
     * Result from activities with pending results
     * @param requestCode int
     * @param resultCode int
     * @param data Intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RSS_DOWNLOAD_REQUEST_CODE) {
            handleRSS(data.getStringExtra(RSSReader.INPUTSOURCE_EXTRA));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Creates the actionbar meny
     * @param menu Menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * When a item is selected from a listView
     * @param item MenuItem
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int i = item.getItemId();
        if (i == R.id.menu_item) {
            preferences();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Handles a string resource and creates the items in a listView. In this instance the RSS items
     * from a HTTP response
     * @param data String
     */
    private void handleRSS(String data) {
        if(data == null) {
            return;
        }

        final InputSource inputSource = new InputSource(new StringReader(data));
        RSSHandler handler = new RSSHandler(this.limit);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(inputSource, handler);
            feedItems = handler.getRssItems();
            mListView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, feedItems));
            mListView.setOnItemClickListener(new ItemClickListener());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

    }

    /**
     * Launch preferences activity
     */
    public void preferences(){
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(intent);
    }

    /**
     * Fetch data from the configured RSS site
     * @throws IOException Exception
     */
    void fetchUpdate() throws IOException{
        PendingIntent pendingResult = createPendingResult(
                RSS_DOWNLOAD_REQUEST_CODE, new Intent(), 0);
        Intent rssReader = new Intent(getApplicationContext(), RSSReader.class);
        rssReader.putExtra(RSSReader.PENDING_RESULT_EXTRA, pendingResult);
        startService(rssReader);
    }

    /**
     * When a RSS item is pressed launch a webView with the corresponding url
     */
    private class ItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String url = feedItems.get(position).getLink();
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }
}
