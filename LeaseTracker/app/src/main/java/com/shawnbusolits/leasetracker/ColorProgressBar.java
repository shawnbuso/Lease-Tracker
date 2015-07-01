package com.shawnbusolits.leasetracker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * Created by sbusolits on 7/1/15.
 */
public class ColorProgressBar extends ProgressBar {

    private static final double WARN_THRESHOLD_PERCENTAGE = 0.90;

    public ColorProgressBar(Context context) {
        super(context);
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setProgress(int progress) {
        Resources res = getContext().getResources();
        Rect bounds = getProgressDrawable().getBounds();
        if (progress > getMax()) {
            setProgressDrawable(res.getDrawable(R.drawable.redprogressbar));
        } else if (progress >= (getMax() * WARN_THRESHOLD_PERCENTAGE)) {
            setProgressDrawable(res.getDrawable(R.drawable.yellowprogressbar));
        } else {
            setProgressDrawable(res.getDrawable(R.drawable.greenprogressbar));
        }
        getProgressDrawable().setBounds(bounds);
        super.setProgress(progress);
    }

    public void setProgress(int progress, int allowedProgress) {
        Resources res = getContext().getResources();
        Rect bounds = getProgressDrawable().getBounds();
        if (progress > allowedProgress) {
            setProgressDrawable(res.getDrawable(R.drawable.redprogressbar));
        } else if (progress >= (allowedProgress * WARN_THRESHOLD_PERCENTAGE)) {
            setProgressDrawable(res.getDrawable(R.drawable.yellowprogressbar));
        } else {
            setProgressDrawable(res.getDrawable(R.drawable.greenprogressbar));
        }
        getProgressDrawable().setBounds(bounds);
        super.setProgress(progress);
    }
}
