package com.youga.recyclerwrapper.view;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.youga.recyclerwrapper.R;

/**
 * Created by Youga on 2017/8/18.
 */

public class FillView extends FrameLayout {

    public FillView(@NonNull Context context) {
        this(context, null);
    }

    public FillView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FillView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleInverse);
        FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        addView(progressBar, params);


        TextView textView = new TextView(context);
        textView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        addView(textView, params);
    }


    public void showEmpty() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                view.setVisibility(VISIBLE);
                ((TextView) view).setText(getResources().getString(R.string.string_empty));
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null, getAppIcon(), null, null);
            } else if (view instanceof ProgressBar) {
                view.setVisibility(GONE);
            }
        }
    }

    public void showError() {
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                view.setVisibility(VISIBLE);
                ((TextView) view).setText(getResources().getString(R.string.string_error));
                ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(null, getAppIcon(), null, null);
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

    public Drawable getAppIcon() {
        try {
            ApplicationInfo info = getContext().getPackageManager().getApplicationInfo(getContext().getPackageName(), 0);
            return info.loadIcon(getContext().getPackageManager());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}
