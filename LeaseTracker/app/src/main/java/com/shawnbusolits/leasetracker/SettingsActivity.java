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

    EditText mStartBox;
    EditText mTermBox;
    EditText mMilesDeliveredBox;
    EditText mMilesAllowedBox;
    EditText mOverageChargeBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mLeaseData = LeaseData.getInstance(this);

        // Attach submit listener
        final Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(new SubmitListener());

        mStartBox = (EditText) findViewById(R.id.start_box);
        mTermBox = (EditText) findViewById(R.id.term_box);
        mMilesDeliveredBox = (EditText) findViewById(R.id.miles_delivered_box);
        mMilesAllowedBox = (EditText) findViewById(R.id.miles_allowed_box);
        mOverageChargeBox = (EditText) findViewById(R.id.overage_charge_box);

        mStartBox.setText(mLeaseData.getStartDateString());
        mTermBox.setText(Integer.toString(mLeaseData.getTermLength()));
        mMilesDeliveredBox.setText(Float.toString(mLeaseData.getMilesDelivered()));
        mMilesAllowedBox.setText(Float.toString(mLeaseData.getMilesAllowed()));
        mOverageChargeBox.setText(Float.toString(mLeaseData.getOverageCharge()));

    }

    private class SubmitListener implements View.OnClickListener {

        public void onClick(View v) {
            mLeaseData.setStartDate(mStartBox.getText().toString());
            mLeaseData.setTermLength(Integer.parseInt(mTermBox.getText().toString()));
            mLeaseData.setMilesDelivered(Float.parseFloat(mMilesDeliveredBox.getText().toString()));
            mLeaseData.setMilesAllowed(Float.parseFloat(mMilesAllowedBox.getText().toString()));
            mLeaseData.setOverageCharge(Float.parseFloat(mOverageChargeBox.getText().toString()));

            mLeaseData.saveLeaseData();

            setResult(Activity.RESULT_OK);
            finish();
        }
    }
}
