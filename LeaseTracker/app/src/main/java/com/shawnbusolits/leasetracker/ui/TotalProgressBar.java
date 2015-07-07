package com.shawnbusolits.leasetracker.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Shawn on 7/3/2015.
 */
public class TotalProgressBar extends SuperProgressBar {

    protected int mCurrentAllowed;

    public TotalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress, int total, int currentAllowed) {
        mProgress = progress;
        mTotal = total;
        mCurrentAllowed = currentAllowed;
        mProgressBar.setProgress(progress, currentAllowed);
        layoutVerticalLine();
    }

    protected void layoutVerticalLine() {
        double marginThroughLease = ((double) mCurrentAllowed) / mTotal;
        mVerticalLineMargin = (int) (marginThroughLease * mWidth);
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams) mVerticalLine.getLayoutParams();
        layoutParams.setMargins(mVerticalLineMargin, dpToPx(15), 0, 0);
        mVerticalLine.setLayoutParams(layoutParams);
    }
}
