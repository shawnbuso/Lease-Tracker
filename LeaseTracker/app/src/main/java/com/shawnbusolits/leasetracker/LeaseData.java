package com.shawnbusolits.leasetracker;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sbusolits on 7/1/15.
 */
public class LeaseData {

    private static LeaseData INSTANCE = null;

    public static final String FLOAT_FORMAT = "%.2f";

    private static final String PREFS_NAME = "LeaseTrackerPrefs";
    private static final String START_DATE_ID = "start_date";
    private static final String TERM_ID = "term";
    private static final String MILES_DELIVERED_ID = "miles_delivered";
    private static final String MILES_ALLOWED_ID = "miles_allowed";
    private static final String MILES_CURRENT_ID = "miles_current";

    private Context mContext;
    private Date mStartDate;
    private int mTermLength;
    private float mMilesDelivered;
    private float mMilesAllowed;
    private float mMilesCurrent;

    private SimpleDateFormat mDateFormatter = null;

    public LeaseData(Context context) {
        mContext = context;
        mDateFormatter = new SimpleDateFormat("MM/dd/yyyy");
        loadLeaseData();
    }

    public static synchronized LeaseData getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LeaseData(context);
        }
        return INSTANCE;
    }

    public Date getStartDate() {
        return mStartDate;
    }

    public String getStartDateString() {
        return mStartDate == null ? "" : mDateFormatter.format(mStartDate);
    }

    public void setStartDate(String startDate) {
        try {
            mStartDate = mDateFormatter.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getTermLength() {
        return mTermLength;
    }

    public void setTermLength(int termLength) {
        mTermLength = termLength;
    }

    public float getMilesDelivered() {
        return mMilesDelivered;
    }

    public String getMilesDeliveredString() {
        return String.format(FLOAT_FORMAT, mMilesDelivered);
    }

    public void setMilesDelivered(float milesDelivered) {
        mMilesDelivered = milesDelivered;
    }

    public float getMilesAllowed() {
        return mMilesAllowed;
    }

    public String getMilesAllowedString() {
        return String.format(FLOAT_FORMAT, mMilesAllowed);
    }

    public float getTotalMilesAllowed() {
        int totalMilesAllowed = (int) (mMilesAllowed * (mTermLength / 12));
        return totalMilesAllowed;
    }

    public String getTotalMilesAllowedString() {
        return String.format(FLOAT_FORMAT, getTotalMilesAllowed());
    }

    public void setMilesAllowed(float milesAllowed) {
        mMilesAllowed = milesAllowed;
    }

    public float getMilesCurrent() {
        return mMilesCurrent;
    }

    public String getMilesCurrentString() {
        return String.format(FLOAT_FORMAT, mMilesCurrent);
    }

    public void setMilesCurrent(float milesCurrent) {
        mMilesCurrent = milesCurrent;
    }

    public void loadLeaseData() {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        String startDateString = settings.getString(START_DATE_ID, "");
        try {
            mStartDate = mDateFormatter.parse(startDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mTermLength = settings.getInt(TERM_ID, 0);
        mMilesDelivered = settings.getFloat(MILES_DELIVERED_ID, 0);
        mMilesAllowed = settings.getFloat(MILES_ALLOWED_ID, 0);
        mMilesCurrent = settings.getFloat(MILES_CURRENT_ID, 0);
    }

    public void saveLeaseData() {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putFloat(MILES_CURRENT_ID, mMilesCurrent);
        editor.putString(START_DATE_ID, getStartDateString());
        editor.putInt(TERM_ID, mTermLength);
        editor.putFloat(MILES_DELIVERED_ID, mMilesDelivered);
        editor.putFloat(MILES_ALLOWED_ID, mMilesAllowed);
        editor.commit();
    }
}
