package com.shawnbusolits.leasetracker.data;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sbusolits on 7/1/15.
 */
public class LeaseData {

    public static final String FLOAT_FORMAT = "%.2f";
    private static final String PREFS_NAME = "LeaseTrackerPrefs";
    private static final String START_DATE_ID = "start_date";
    private static final String TERM_ID = "term";
    private static final String MILES_DELIVERED_ID = "miles_delivered";
    private static final String MILES_ALLOWED_ID = "miles_allowed";
    private static final String MILES_CURRENT_ID = "miles_current";
    private static final String OVERAGE_CHARGE_ID = "overage_charge";

    private static LeaseData INSTANCE = null;
    private Context mContext;
    private Date mStartDate;
    private int mTermLength;
    private int mMilesDelivered;
    private int mMilesAllowed;
    private int mMilesCurrent;
    private float mOverageCharge;

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

    public void setStartDate(String startDate) {
        try {
            mStartDate = mDateFormatter.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStartDateString() {
        return mStartDate == null ? "" : mDateFormatter.format(mStartDate);
    }

    public int getTermLength() {
        return mTermLength;
    }

    public void setTermLength(int termLength) {
        mTermLength = termLength;
    }

    public int getMilesDelivered() {
        return mMilesDelivered;
    }

    public void setMilesDelivered(int milesDelivered) {
        mMilesDelivered = milesDelivered;
    }

    public String getMilesDeliveredString() {
        return String.format(FLOAT_FORMAT, mMilesDelivered);
    }

    public int getMilesAllowed() {
        return mMilesAllowed;
    }

    public void setMilesAllowed(int milesAllowed) {
        mMilesAllowed = milesAllowed;
    }

    public String getMilesAllowedString() {
        return String.format(FLOAT_FORMAT, mMilesAllowed);
    }

    public int getTotalMilesAllowed() {
        return ((int) mMilesAllowed * (mTermLength / 12));
    }

    public String getTotalMilesAllowedString() {
        return Integer.toString(getTotalMilesAllowed());
    }

    public int getMilesCurrent() {
        return mMilesCurrent;
    }

    public void setMilesCurrent(int milesCurrent) {
        mMilesCurrent = milesCurrent;
    }

    public String getMilesCurrentString() {
        return Integer.toString(mMilesCurrent);
    }

    public void setOverageCharge(float overageCharge) {
        mOverageCharge = overageCharge;
    }

    public double getOverageCharge() {
        return mOverageCharge;
    }

    public String getOverageChargeString() {
        return String.format(FLOAT_FORMAT, mOverageCharge);
    }

    public Date getEndDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        calendar.add(Calendar.MONTH, mTermLength);
        return calendar.getTime();
    }

    public int getDaysInLease() {
        long startTime = mStartDate.getTime();
        long endTime = getEndDate().getTime();
        long timeDiff = endTime - startTime;
        return (int) (timeDiff / (1000 * 60 * 60 * 24)) + 1;
    }

    public int getDaysSinceStart() {
        long startTime = mStartDate.getTime();
        long endTime = new Date().getTime();
        long timeDiff = endTime - startTime;
        return (int) (timeDiff / (1000 * 60 * 60 * 24)) + 1;
    }

    public double getCurrentAllowedMiles() {
        return getAllowedMilesPerDay() * getDaysSinceStart();
    }

    public double getAllowedMilesPerDay() {
        return ((float)getTotalMilesAllowed()) / getDaysInLease();
    }

    public double getExpectedMiles() {
        return getMilesPerDay() * getDaysInLease();
    }

    public double getMilesPerDay() {
        return ((double)(mMilesCurrent - mMilesDelivered)) / getDaysSinceStart();
    }

    public double getAllowedMilesToPresent() {
        return getAllowedMilesPerDay() * getDaysSinceStart();
    }

    public String getOverageTotalChargeString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        return formatter.format((getExpectedMiles() - (getTotalMilesAllowed() + mMilesDelivered)) * mOverageCharge);
    }

    public double getDaysTillRunout() {
        return getTotalMilesAllowed() / getMilesPerDay();
    }

    public Date getMileageRunoutDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mStartDate);
        calendar.add(Calendar.DATE, (int)getDaysTillRunout());
        return calendar.getTime();
    }

    public String getMileageRunoutDateString() {
        return mDateFormatter.format(getMileageRunoutDate());
    }

    public int getOverageDayGap() {
        return (int)(getDaysInLease() - getDaysTillRunout());
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
        mMilesDelivered = settings.getInt(MILES_DELIVERED_ID, 0);
        mMilesAllowed = settings.getInt(MILES_ALLOWED_ID, 0);
        mMilesCurrent = settings.getInt(MILES_CURRENT_ID, 0);
        mOverageCharge = settings.getFloat(OVERAGE_CHARGE_ID, 0);
    }

    public void saveLeaseData() {
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(MILES_CURRENT_ID, mMilesCurrent);
        editor.putString(START_DATE_ID, getStartDateString());
        editor.putInt(TERM_ID, mTermLength);
        editor.putInt(MILES_DELIVERED_ID, mMilesDelivered);
        editor.putInt(MILES_ALLOWED_ID, mMilesAllowed);
        editor.putFloat(OVERAGE_CHARGE_ID, mOverageCharge);
        editor.commit();
    }
}
