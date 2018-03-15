package com.ntnu.mobiledev.mj.lab2rssreader;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by markusja on 3/9/18.
 */

public class RSSReader_v2 extends AsyncTask<Context, String, String> {
    private static final String PREFS_NAME = "PrefsFile";
    private static final String TAG = RSSReader.class.getSimpleName();
    private OkHttpClient client;
    private Context context;

    public RSSReader_v2(){
        client = new OkHttpClient();
    }

    @Override
    protected String doInBackground(Context... contexts) {
        context = contexts[0];

        final HttpUrl url = buildURLFromPreference();
        if(Objects.equals(url, null)){
            return null;
        }


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
                    return body;
                }
            } else {
                Log.d("Http code", String.valueOf(responseCode));
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Builds a HttpUrl object from preferences
     * @return HttpUrl
     */
    HttpUrl buildURLFromPreference(){
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        final String urlFromPreference = preferences.getString("URL", PreferencesActivity.DEFAULT_URL);

        if(Objects.equals(urlFromPreference, "")){
            return null;
        } else {
            return HttpUrl.parse(urlFromPreference);
        }
    }
}
