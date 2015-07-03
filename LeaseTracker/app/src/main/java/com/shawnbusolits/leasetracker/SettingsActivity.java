package com.shawnbusolits.leasetracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by sbusolits on 6/30/15.
 */
public class SettingsActivity extends AppCompatActivity {

    private LeaseData mLeaseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLeaseData = LeaseData.getInstance(this);

        // Attach submit listener
        final Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new SubmitListener());

        EditText startBox = (EditText) findViewById(R.id.start_box);
        EditText termBox = (EditText) findViewById(R.id.term_box);
        EditText milesDeliveredBox = (EditText) findViewById(R.id.miles_delivered_box);
        EditText milesAllowedBox = (EditText) findViewById(R.id.miles_allowed_box);

        startBox.setText(mLeaseData.getStartDateString());
        termBox.setText(Integer.toString(mLeaseData.getTermLength()));
        milesDeliveredBox.setText(Float.toString(mLeaseData.getMilesDelivered()));
        milesAllowedBox.setText(Float.toString(mLeaseData.getMilesAllowed()));

    }

    private class SubmitListener implements View.OnClickListener {

        public void onClick(View v) {
            // Get handle to inputs
            final EditText startBox = (EditText) findViewById(R.id.start_box);
            final EditText termBox = (EditText) findViewById(R.id.term_box);
            final EditText milesDeliveredBox = (EditText) findViewById(R.id.miles_delivered_box);
            final EditText milesAllowedBox = (EditText) findViewById(R.id.miles_allowed_box);

            mLeaseData.setStartDate(startBox.getText().toString());
            mLeaseData.setTermLength(Integer.parseInt(termBox.getText().toString()));
            mLeaseData.setMilesDelivered(Float.parseFloat(milesDeliveredBox.getText().toString()));
            mLeaseData.setMilesAllowed(Float.parseFloat(milesAllowedBox.getText().toString()));

            mLeaseData.saveLeaseData();

            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
