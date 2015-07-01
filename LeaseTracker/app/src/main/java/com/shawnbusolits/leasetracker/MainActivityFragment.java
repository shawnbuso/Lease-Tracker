package com.shawnbusolits.leasetracker;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TextWatcher {

    private EditText mCurrentMilesBox;
    private LeaseData mLeaseData;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getLeaseData();
        mCurrentMilesBox = (EditText) getActivity().findViewById(R.id.current_miles);
        mCurrentMilesBox.setText(Float.toString(mLeaseData.getMilesCurrent()));
        mCurrentMilesBox.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void getLeaseData() {
        if (mLeaseData == null) {
            mLeaseData = LeaseData.getInstance(getActivity());
        }
        updateUI();
    }

    public void updateUI() {
        if (mLeaseData.getStartDate() != null &&
            mLeaseData.getTermLength() > 0 &&
            mLeaseData.getMilesAllowed() > 0) {
            // Handle to UI things
            ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
            View verticalLine = (View) getActivity().findViewById(R.id.vertical_line);
            TextView expectedText = (TextView) getActivity().findViewById(R.id.expected_text_view);

            double maxMiles = mLeaseData.getMilesDelivered() + mLeaseData.getTotalMilesAllowed();
            int daysInLease = getDaysInLease();
            int daysSinceStart = getDaysSinceStart();
            double allowedMilesPerDay = ((double) mLeaseData.getTotalMilesAllowed()) / daysInLease;
            double currentAllowedMiles = allowedMilesPerDay * daysSinceStart;
            double milesPerDay =
                    (mLeaseData.getMilesCurrent() - mLeaseData.getMilesDelivered()) / daysSinceStart;
            double expectedMiles = milesPerDay * daysInLease;

            progressBar.setMax((int)mLeaseData.getTotalMilesAllowed());
            progressBar.setProgress(
                    (int)(mLeaseData.getMilesCurrent() - mLeaseData.getMilesDelivered()));

            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) verticalLine.getLayoutParams();
            int lineMargin =
                    (int) ((daysSinceStart * allowedMilesPerDay) / mLeaseData.getTotalMilesAllowed()) * progressBar.getWidth();
            layoutParams.setMargins(lineMargin, dpToPx(15), 0, 0);

            expectedText.setText(
                    getActivity().getResources().getString(R.string.expected_text) +
                    expectedMiles + " of " + mLeaseData.getTotalMilesAllowed() + "\n" +
                    "CurrentAllowedMiles: " + currentAllowedMiles + "\n" +
                    "Miles per day: " + milesPerDay + "\n" +
                    "Allowed miles per day: " + allowedMilesPerDay);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getActivity().getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    private Date getEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mLeaseData.getStartDate());
        calendar.add(Calendar.MONTH, mLeaseData.getTermLength());
        return calendar.getTime();
    }

    private int getDaysInLease() {
        long startTime = mLeaseData.getStartDate().getTime();
        long endTime = getEndDate().getTime();
        long timeDiff = endTime - startTime;
        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    private int getDaysSinceStart() {
        long startTime = mLeaseData.getStartDate().getTime();
        long endTime = new Date().getTime();
        long timeDiff = endTime - startTime;
        return (int) (timeDiff / (1000 * 60 * 60 * 24)) + 1;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        return;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            mLeaseData.setMilesCurrent(Integer.parseInt(mCurrentMilesBox.getText().toString().trim()));
            mLeaseData.saveLeaseData();
            updateUI();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        return;
    }
}
