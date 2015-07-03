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
        if (progress > total) {
            mProgressBar.setProgress(total);

            mVerticalLine.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) mVerticalLine.getLayoutParams();
            double marginThroughAllowance = progress / total;
            int lineMargin = (int) (marginThroughAllowance * mWidth);
            layoutParams.setMargins(lineMargin, dpToPx(15), 0, 0);
            mVerticalLine.setLayoutParams(layoutParams);
        } else {
            mProgressBar.setProgress(progress);
            mVerticalLine.setVisibility(View.INVISIBLE);
        }
    }
}
