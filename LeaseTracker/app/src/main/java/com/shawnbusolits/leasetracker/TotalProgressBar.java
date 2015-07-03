package com.shawnbusolits.leasetracker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Shawn on 7/3/2015.
 */
public class TotalProgressBar extends SuperProgressBar {

    public TotalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setProgress(int progress, int total, int currentAllowed) {
        mProgressBar.setProgress(progress);
        if (mWidth > 0) {
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) mVerticalLine.getLayoutParams();
            double marginThroughLease = currentAllowed / total;
            int lineMargin = (int) (marginThroughLease * mWidth);
            layoutParams.setMargins(lineMargin, dpToPx(15), 0, 0);
            mVerticalLine.setLayoutParams(layoutParams);
        }
    }
}
