package com.shawnbusolits.leasetracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

    private Date startDate;
    private int termLength;
    private int milesDelivered;
    private int milesAllowed;
    private int milesCurrent;

    private EditText currentMilesBox;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getLeaseData();
        currentMilesBox = (EditText) getActivity().findViewById(R.id.current_miles);
        String milesCurrentString = Integer.toString(milesCurrent);
        currentMilesBox.setText(milesCurrentString);
        currentMilesBox.addTextChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    public void getLeaseData() {
        SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
        String startDateString = settings.getString(MainActivity.START_DATE_ID, "");
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        try {
            startDate = formatter.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        termLength = settings.getInt(MainActivity.TERM_ID, 0);
        milesDelivered = settings.getInt(MainActivity.MILES_DELIVERED_ID, 0);
        milesAllowed = settings.getInt(MainActivity.MILES_ALLOWED_ID, 0);
        milesCurrent = settings.getInt(MainActivity.MILES_CURRENT_ID, 0);
        updateUI();
    }

    public void updateUI() {
        if (startDate != null && termLength > 0 && milesAllowed > 0) {
            // Handle to UI things
            ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progress_bar);
            View verticalLine = (View) getActivity().findViewById(R.id.vertical_line);
            TextView expectedText = (TextView) getActivity().findViewById(R.id.expected_text_view);



            // Max miles
            // Current miles
            // Current allowed miles
            // Expected miles

            int maxMiles = milesDelivered + milesAllowed;
            int daysInLease = getDaysInLease();
            int daysSinceStart = getDaysSinceStart();
            double allowedMilesPerDay = ((double) milesAllowed) / daysInLease;
            double currentAllowedMiles = allowedMilesPerDay * daysSinceStart;
            double milesPerDay = (((double)(milesCurrent - milesDelivered)) / daysSinceStart);
            double expectedMiles = milesPerDay * daysInLease;

            progressBar.setMax(milesAllowed);
            progressBar.setProgress(milesCurrent - milesDelivered);

            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) verticalLine.getLayoutParams();
            int lineMargin = (int) ((daysSinceStart * allowedMilesPerDay) / milesAllowed) * progressBar.getWidth();
            layoutParams.setMargins(lineMargin, dpToPx(15), 0, 0);

            expectedText.setText(
                    getActivity().getResources().getString(R.string.expected_text) +
                    expectedMiles + " of " + milesAllowed + "\n" +
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
        calendar.setTime(startDate);
        calendar.add(Calendar.MONTH, termLength);
        return calendar.getTime();
    }

    private int getDaysInLease() {
        long startTime = startDate.getTime();
        long endTime = getEndDate().getTime();
        long timeDiff = endTime - startTime;
        return (int) (timeDiff / (1000 * 60 * 60 * 24));
    }

    private int getDaysSinceStart() {
        long startTime = startDate.getTime();
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
            milesCurrent = Integer.parseInt(currentMilesBox.getText().toString().trim());
            SharedPreferences settings = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt(MainActivity.MILES_CURRENT_ID, milesCurrent);
            editor.commit();
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
