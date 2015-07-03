package com.shawnbusolits.leasetracker;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Shawn on 7/3/2015.
 */
public abstract class SuperProgressBar extends RelativeLayout {

    protected ColorProgressBar mProgressBar;
    protected View mVerticalLine;
    protected TextView mTextView;

    protected int mProgress;
    protected int mTotal;

    protected int mWidth;
    protected int mVerticalLineMargin;

    public SuperProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.super_progress_bar, this);

        mProgressBar = (ColorProgressBar) findViewById(R.id.progress_bar);
        mVerticalLine = findViewById(R.id.vertical_line);
        mTextView = (TextView) findViewById(R.id.progress_text_view);
    }

    public void setMax(int max) {
        mProgressBar.setMax(max);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    protected int dpToPx(int dp) {
        Resources r = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        super.onSizeChanged(xNew, yNew, xOld, yOld);

        mWidth = xNew;

        layoutVerticalLine();
    }

    protected abstract void layoutVerticalLine();
}
