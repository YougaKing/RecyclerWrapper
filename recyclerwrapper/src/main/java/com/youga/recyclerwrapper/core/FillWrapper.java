package com.youga.recyclerwrapper.core;

import android.support.annotation.IntDef;
import android.view.View;

import com.youga.recyclerwrapper.view.FillViewProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class FillWrapper<T extends View, K> {

    public static final int NONE = 16843830, LOAD = NONE + 1, EMPTY = LOAD + 1, ERROR = EMPTY + 1;
    private int mType = NONE;

    @IntDef({LOAD, EMPTY, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private K k;

    private FillViewProvider<T, K> fillView;

    public FillViewProvider<T, K> getFillView() {
        return fillView;
    }

    public void setFillView(FillViewProvider<T, K> fillView) {
        this.fillView = fillView;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }
}
