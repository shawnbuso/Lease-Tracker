package com.shawnbusolits.leasetracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by sbusolits on 6/30/15.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Attach submit listener
        final Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new SubmitListener());

        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String startDateString = settings.getString(MainActivity.START_DATE_ID, "");
        int termLength = settings.getInt(MainActivity.TERM_ID, 0);
        int milesDelivered = settings.getInt(MainActivity.MILES_DELIVERED_ID, 0);
        int milesAllowed = settings.getInt(MainActivity.MILES_ALLOWED_ID, 0);

        EditText startBox = (EditText) findViewById(R.id.start_box);
        EditText termBox = (EditText) findViewById(R.id.term_box);
        EditText milesDeliveredBox = (EditText) findViewById(R.id.miles_delivered_box);
        EditText milesAllowedBox = (EditText) findViewById(R.id.miles_allowed_box);

        startBox.setText(startDateString);
        termBox.setText(termLength);
        milesDeliveredBox.setText(milesDelivered);
        milesAllowedBox.setText(milesAllowed);

    }

    private class SubmitListener implements View.OnClickListener {

        public void onClick(View v) {
            // Get handle to inputs
            final EditText startBox = (EditText) findViewById(R.id.start_box);
            final EditText termBox = (EditText) findViewById(R.id.term_box);
            final EditText milesDeliveredBox = (EditText) findViewById(R.id.miles_delivered_box);
            final EditText milesAllowedBox = (EditText) findViewById(R.id.miles_allowed_box);

            // Pass data back to main activity
            Intent resultIntent = new Intent();
            resultIntent.putExtra(MainActivity.START_DATE_ID, startBox.getText().toString());
            resultIntent.putExtra(
                    MainActivity.TERM_ID, Integer.parseInt(termBox.getText().toString()));
            resultIntent.putExtra(
                    MainActivity.MILES_DELIVERED_ID,
                    Integer.parseInt(milesDeliveredBox.getText().toString()));
            resultIntent.putExtra(
                    MainActivity.MILES_ALLOWED_ID,
                    Integer.parseInt(milesAllowedBox.getText().toString()));

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }
}
