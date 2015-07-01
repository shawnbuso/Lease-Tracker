package com.shawnbusolits.leasetracker;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ViewTreeObserver.OnGlobalLayoutListener, TextWatcher {

    private ColorProgressBar mTotalProgressBar;
    private EditText mCurrentMilesBox;

    private int mTotalProgressBarWidth = 0;
    private LeaseData mLeaseData;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        getLeaseData();
        mTotalProgressBar = (ColorProgressBar) getActivity().findViewById(R.id.total_progress_bar);
        mTotalProgressBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mCurrentMilesBox = (EditText) getActivity().findViewById(R.id.current_miles);
        mCurrentMilesBox.setText(Float.toString(mLeaseData.getMilesCurrent()));
        mCurrentMilesBox.addTextChangedListener(this);
        updateUI();
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
    }

    public void updateUI() {
        if (mLeaseData.getStartDate() != null &&
            mLeaseData.getTermLength() > 0 &&
            mLeaseData.getMilesAllowed() > 0) {
            // Handle to UI things
            View verticalLine = (View) getActivity().findViewById(R.id.vertical_line);
            TextView totalText = (TextView) getActivity().findViewById(R.id.total_text_view);
            ColorProgressBar currentProgressBar = (ColorProgressBar) getActivity().findViewById(R.id.current_progress_bar);
            TextView currentText = (TextView) getActivity().findViewById(R.id.current_text_view);
            TextView dailyText = (TextView) getActivity().findViewById(R.id.daily_text_view);


            mTotalProgressBar.setMax((int) mLeaseData.getTotalMilesAllowed());
            mTotalProgressBar.setProgress(
                    (int) (mLeaseData.getMilesCurrent() - mLeaseData.getMilesDelivered()),
                    (int) mLeaseData.getCurrentAllowedMiles());

            if (mTotalProgressBarWidth > 0) {
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams) verticalLine.getLayoutParams();
                double allowedMilesToToday = mLeaseData.getAllowedMilesPerDay() * mLeaseData.getDaysSinceStart();
                double marginThroughLease = allowedMilesToToday / mLeaseData.getTotalMilesAllowed();
                int lineMargin = (int) (marginThroughLease * mTotalProgressBarWidth);
                layoutParams.setMargins(lineMargin, dpToPx(15), 0, 0);
                verticalLine.setLayoutParams(layoutParams);
            }

            totalText.setText(
                    getActivity().getResources().getString(R.string.expected_text) +
                            String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getExpectedMiles()) +
                            " of " + mLeaseData.getTotalMilesAllowedString());

            currentProgressBar.setMax((int) mLeaseData.getCurrentAllowedMiles());
            currentProgressBar.setProgress((int) mLeaseData.getMilesCurrent());
            currentText.setText("Current mileage: " +
                    mLeaseData.getMilesCurrentString() + " of " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getCurrentAllowedMiles()));
            dailyText.setText("Daily mileage: " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getMilesPerDay()) + " of " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getAllowedMilesPerDay()));

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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        return;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        try {
            mLeaseData.setMilesCurrent(Float.parseFloat(mCurrentMilesBox.getText().toString().trim()));
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

    @Override
    public void onGlobalLayout() {
        mTotalProgressBarWidth = mTotalProgressBar.getWidth();
        updateUI();
    }
}
