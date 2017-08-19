package com.youga.recyclerwrapper.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Youga on 2017/8/18.
 */

public class FootView extends LinearLayout {

    public FootView(@NonNull Context context) {
        this(context, null);
    }

    public FootView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FootView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        setGravity(Gravity.CENTER);
        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        addView(progressBar, params);


        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(context);
        addView(textView, params);
    }


    public void showFault(String text) {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                view.setVisibility(VISIBLE);
                ((TextView) view).setText(text);
            } else if (view instanceof ProgressBar) {
                view.setVisibility(GONE);
            }
        }
    }

    public void showLoading() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                view.setVisibility(GONE);
            } else if (view instanceof ProgressBar) {
                view.setVisibility(VISIBLE);
            }
        }
    }
}
