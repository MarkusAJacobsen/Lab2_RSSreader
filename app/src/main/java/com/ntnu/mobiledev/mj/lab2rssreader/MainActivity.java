package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.HttpUrl;

public class MainActivity extends AppCompatActivity {
    private static final int RSS_DOWNLOAD_REQUEST_CODE = 0;
    private ActionBar mActionBar;
    private ListView mListView;

    private HttpUrl url;
    private List<FeedItem> feedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);

        mListView = findViewById(R.id.listViewFeed);

        try {
            fetchUpdate();
        } catch(IOException e){
            e.printStackTrace();
        }



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
        final InputSource inputSource = new InputSource(new StringReader(data));
        RSSHandler handler = new RSSHandler();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        try {
            saxParser = factory.newSAXParser();
            saxParser.parse(inputSource, handler);
            feedItems = handler.getRssItems();
            mListView.setAdapter(new ArrayAdapter<>(this, R.layout.list_item, feedItems));
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
}
