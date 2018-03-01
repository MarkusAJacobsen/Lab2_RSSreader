package com.ntnu.mobiledev.mj.lab2rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

/**
 * Created by markusja on 2/12/18.
 */

public class WebActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        final String url = intent.getStringExtra("url");
        mWebView = findViewById(R.id.browser);

        mWebView.loadUrl(url);
    }
}
