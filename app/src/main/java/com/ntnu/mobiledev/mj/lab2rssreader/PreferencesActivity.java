package com.ntnu.mobiledev.mj.lab2rssreader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by markusja on 2/7/18.
 */

public class PreferencesActivity extends AppCompatActivity {
    private Button mSave;
    private Button mDiscard;
    private EditText mUrl;
    private Spinner mFeed;
    private Spinner mRefresh;

    public static final String PREFS_NAME = "PrefsFile";
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

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mDiscard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(PreferencesActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
