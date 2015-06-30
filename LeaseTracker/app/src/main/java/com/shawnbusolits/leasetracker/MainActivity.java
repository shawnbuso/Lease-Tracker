package com.shawnbusolits.leasetracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    final int SETTINGS_REQUEST_CODE = 1;

    public static final String PREFS_NAME = "LeaseTrackerPRefs";
    public static final String START_DATE_ID = "start_date";
    public static final String TERM_ID = "term";
    public static final String MILES_DELIVERED_ID = "miles_delivered";
    public static final String MILES_ALLOWED_ID = "miles_allowed";
    public static final String MILES_CURRENT_ID = "miles_current";

    private MainActivityFragment mainFragment;

    private String startDateString;
    private int termLength;
    private int milesDelivered;
    private int milesAllowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mainFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showOptions();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showOptions() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivityForResult(intent, SETTINGS_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case SETTINGS_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    parseSettingsData(data);
                    writeLeaseData();
                }
                break;
        }
    }

    private void parseSettingsData(Intent data) {
        startDateString = data.getStringExtra(START_DATE_ID);
        termLength = data.getIntExtra(TERM_ID, 0);
        milesDelivered = data.getIntExtra(MILES_DELIVERED_ID, 0);
        milesAllowed = data.getIntExtra(MILES_ALLOWED_ID, 0);
    }

    private void writeLeaseData() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(START_DATE_ID, startDateString);
        editor.putInt(TERM_ID, termLength);
        editor.putInt(MILES_DELIVERED_ID, milesDelivered);
        editor.putInt(MILES_ALLOWED_ID, milesAllowed * (termLength / 12));
        editor.commit();
    }
}
