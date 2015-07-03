package com.shawnbusolits.leasetracker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Shawn on 7/3/2015.
 */
public class DailyProgressBar extends SuperProgressBar {

    public DailyProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress, int total) {
        mProgress = progress;
        mTotal = total;
        if (progress > total) {
            mProgressBar.setProgress(mTotal);
            layoutVerticalLine();
            mVerticalLine.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setProgress(mProgress);
            mVerticalLine.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void layoutVerticalLine() {
        double marginThroughAllowance = ((double) mTotal) / mProgress;
        mVerticalLineMargin = (int) (marginThroughAllowance * mWidth);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mVerticalLine.getLayoutParams();
        layoutParams.setMargins(mVerticalLineMargin, dpToPx(15), 0, 0);
        mVerticalLine.setLayoutParams(layoutParams);
    }
}
