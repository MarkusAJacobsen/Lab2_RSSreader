package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;
    private ActionBar mActionBar;
    private ListView mListView;
    private int limit;
    private HttpUrl url;
    private List<FeedItem> feedItems;

    private static final int DEFAULT_LIMIT = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);

        mListView = findViewById(R.id.listViewFeed);

        getLimitFromPreferences();

        try {
            fetchUpdate();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void getLimitFromPreferences() {
        final SharedPreferences preferences = getSharedPreferences(PreferencesActivity.PREFS_NAME, Context.MODE_PRIVATE);
        this.limit = preferences.getInt(PreferencesActivity.PREFS_LIMIT, DEFAULT_LIMIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RSS_DOWNLOAD_REQUEST_CODE) {
            handleRSS(data.getStringExtra(RSSReader.INPUTSOURCE_EXTRA));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

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

    public void preferences(){
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(intent);
    }

    void fetchUpdate() throws IOException{
        PendingIntent pendingResult = createPendingResult(
                RSS_DOWNLOAD_REQUEST_CODE, new Intent(), 0);
        Intent rssReader = new Intent(getApplicationContext(), RSSReader.class);
        rssReader.putExtra(RSSReader.PENDING_RESULT_EXTRA, pendingResult);
        startService(rssReader);
    }

    private class ItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.i("Item pressed", String.valueOf(position));
            Log.i("Url", feedItems.get(position).getLink());

            String url = feedItems.get(position).getLink();
            Intent intent = new Intent(MainActivity.this, WebActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
    }
}
