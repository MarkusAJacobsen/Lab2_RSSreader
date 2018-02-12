package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.content.ContentValues.TAG;

/**
 * Created by markusja on 2/8/18.
 */

public class RSSReader extends IntentService{
    private static final String PREFS_NAME = "PrefsFile";
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
                    Log.d("Response packet", body);
                }
            } else {
                Log.d("Http code", String.valueOf(responseCode));
            }
        } catch (IOException e) {
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
