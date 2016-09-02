package com.ashutosh.sunshinev2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final String FORECASTFRAGMENT_TAG = "FFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.root_main, new ForecastFragment(), FORECASTFRAGMENT_TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);       // inflate the menu xml to the action bar
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_view_map){
            openPreferredLocationInMap();
            return true;
        }else if(id == R.id.action_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openPreferredLocationInMap(){
        String location = Utility.getPreferredLocation(this);
        // using the data URI scheme to view Map by launching an intent
        Uri uri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q",location)
                .build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        if(i.resolveActivity(getPackageManager())!=null){
            startActivity(i);
        }else{
            Log.d(LOG_TAG, "Couldn't call " + location + "No Receiving Apps installed!");
        }
    }

}
