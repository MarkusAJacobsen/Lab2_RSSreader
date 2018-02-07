package com.ntnu.mobiledev.mj.lab2rssreader;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBar = getSupportActionBar();
        mActionBar.setHomeButtonEnabled(true);
        mActionBar.setDisplayHomeAsUpEnabled(true);


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

    public void preferences(){
        Intent intent = new Intent(MainActivity.this, PreferencesActivity.class);
        startActivity(intent);
    }
}
