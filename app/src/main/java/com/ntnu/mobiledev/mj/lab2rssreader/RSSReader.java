package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * Created by markusja on 2/8/18.
 */

public class RSSReader extends IntentService{
    private static final String PREFS_NAME = "PrefsFile";
    private static final String TAG = RSSReader.class.getSimpleName();

    public static final String PENDING_RESULT_EXTRA = "pending_result";
    public static final String INPUTSOURCE_EXTRA = "input_source_byteChar";
    public static final int INPUTSOURCE_CODE = 0;
    public static final int FAILURE = 1;

    OkHttpClient client;

    RSSReader() {
        super(TAG);
        client = new OkHttpClient();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final HttpUrl url = buildURLFromPreference();
        if(Objects.equals(url, null)){
            return;
        }

        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT_EXTRA);
        verifyCertificate();

        final Request request = new Request.Builder()
                .get()
                .url(url)
                .build();

        try {
            final Response response = client.newCall(request).execute();
            final String body = response.body().string();
            int responseCode = response.code();

            if (responseCode == 200) {
                if (body != null) {
                    Intent result = new Intent();
                    result.putExtra(INPUTSOURCE_EXTRA, body);
                    reply.send(this, INPUTSOURCE_CODE, result);
                }
            } else {
                Log.d("Http code", String.valueOf(responseCode));
                reply.send(FAILURE);
            }
        } catch (IOException | PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
    }

    HttpUrl buildURLFromPreference(){
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String urlFromPreference = preferences.getString("URL", "");

        if(Objects.equals(urlFromPreference, "")){
            return null;
        } else {
            return HttpUrl.parse(urlFromPreference);
        }
    }

    boolean verifyCertificate(){
        return false;
    }
}
