package com.shawnbusolits.leasetracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements TextWatcher {

    private TotalProgressBar mTotalProgressBar;
    private DailyProgressBar mDailyProgressBar;
    private EditText mCurrentMilesBox;

    private LeaseData mLeaseData;

    public MainActivityFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        getLeaseData();

        mTotalProgressBar = (TotalProgressBar) getActivity().findViewById(R.id.total_progress_bar);
        mDailyProgressBar = (DailyProgressBar) getActivity().findViewById(R.id.daily_progress_bar);
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

            mTotalProgressBar.setMax((int) mLeaseData.getTotalMilesAllowed());
            mTotalProgressBar.setProgress(
                    (int) (mLeaseData.getMilesCurrent() - mLeaseData.getMilesDelivered()),
                    (int) mLeaseData.getTotalMilesAllowed(),
                    (int) mLeaseData.getAllowedMilesToPresent());
            mTotalProgressBar.setText(
                    getActivity().getResources().getString(R.string.expected_text) +
                            String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getExpectedMiles()) +
                            " of " + mLeaseData.getTotalMilesAllowedString());

            mDailyProgressBar.setMax((int) mLeaseData.getCurrentAllowedMiles());
            mDailyProgressBar.setProgress(
                    (int) mLeaseData.getMilesCurrent(),
                    (int) mLeaseData.getCurrentAllowedMiles());
            mDailyProgressBar.setText("Current mileage: " +
                    mLeaseData.getMilesCurrentString() + " of " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getCurrentAllowedMiles()) +
                    "\nDaily mileage: " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getMilesPerDay()) + " of " +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getAllowedMilesPerDay()));

        }
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
}
