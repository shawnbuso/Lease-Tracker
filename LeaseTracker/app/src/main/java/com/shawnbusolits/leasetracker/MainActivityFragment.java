package com.shawnbusolits.leasetracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.shawnbusolits.leasetracker.data.LeaseData;
import com.shawnbusolits.leasetracker.ui.DailyProgressBar;
import com.shawnbusolits.leasetracker.ui.TotalProgressBar;


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

        mCurrentMilesBox.setText(Integer.toString(mLeaseData.getMilesCurrent()));
        mCurrentMilesBox.addTextChangedListener(this);

        updateUI();

        AdView mAdView = (AdView) getActivity().findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
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

            mTotalProgressBar.setMax((int) mLeaseData.getTotalMilesAllowed());
            mTotalProgressBar.setProgress(
                    (int) (mLeaseData.getMilesCurrent() - mLeaseData.getMilesDelivered()),
                    (int) mLeaseData.getTotalMilesAllowed(),
                    (int) mLeaseData.getAllowedMilesToPresent());
            String totalText = getActivity().getResources().getString(R.string.expected_text) +
                    String.format(LeaseData.FLOAT_FORMAT, mLeaseData.getExpectedMiles()) +
                    " of " + mLeaseData.getTotalMilesAllowedString();
            if (mLeaseData.getExpectedMiles() > (mLeaseData.getTotalMilesAllowed() + mLeaseData.getMilesDelivered())) {
                totalText += "\nOverage cost: " + mLeaseData.getOverageTotalChargeString() +
                "\nMileage runout date: " + mLeaseData.getMileageRunoutDateString() +
                "\nDays in overage: " + mLeaseData.getOverageDayGap();
            }
            mTotalProgressBar.setText(totalText);

        }
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if (mLeaseData != null) {
            mLeaseData.saveLeaseData();
        }
    }
}
