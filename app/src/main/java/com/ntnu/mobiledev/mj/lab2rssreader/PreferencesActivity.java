package com.ntnu.mobiledev.mj.lab2rssreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by markusja on 2/7/18.
 */

public class PreferencesActivity extends AppCompatActivity {
    private Button mSave;
    private Button mDiscard;
    private TextView mUrl;
    private Spinner mFeed;
    private Spinner mRefresh;

    private String url;
    private int feed;
    private String refresh;

    public static final String PREFS_NAME = "PrefsFile";
    public static final String PREFS_LIMIT = "Feed";
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);

        mSave = findViewById(R.id.save);
        mDiscard = findViewById(R.id.discard);
        mFeed = findViewById(R.id.feed);
        mUrl = findViewById(R.id.url);
        mRefresh = findViewById(R.id.refresh);

        // Restore preferences
        preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        url = preferences.getString("URL", "");
        feed = preferences.getInt("Feed", 10);
        refresh = preferences.getString("Refresh", "");

        if(!Objects.equals(url, "")) {
            mUrl.setText(url);
        }

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    final URL userInput = new URL(mUrl.getEditableText().toString());
                    userInput.toURI();
                } catch (MalformedURLException | URISyntaxException e) {
                    mUrl.setError("Malformed URL");
                }



                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("URL", mUrl.getEditableText().toString());
                editor.putInt("Feed", parseInteger(mFeed.getSelectedItem().toString()));
                editor.putString("Refresh", mRefresh.getSelectedItem().toString());
                editor.apply();

                final Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        populateSpinner(String.valueOf(feed), mFeed, R.array.itemsFeed);
        populateSpinner(refresh, mRefresh, R.array.refreshInterval);
    }

    void populateSpinner(String selected, Spinner spinner, int resourceArray){
        List<String> list = new ArrayList<>();
        final String[] items = getResources().getStringArray(resourceArray);

        list.addAll(Arrays.asList(items));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);

        if(list.contains(selected)) {
            spinner.setSelection(list.indexOf(selected));
        } else {
            spinner.setSelection(0);
        }
    }

    int parseInteger(String s){
        Pattern intsOnly = Pattern.compile("([+-]?\\d+)([eE][+-]?\\d+)?");
        Matcher makeMatch = intsOnly.matcher(s);
        makeMatch.find();
        String inputInt = makeMatch.group();
        return Integer.parseInt(inputInt);
    }
}
