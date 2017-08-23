package com.youga.recyclerwrapper.core;

import android.support.annotation.IntDef;
import android.view.View;

import com.youga.recyclerwrapper.view.FillView;
import com.youga.recyclerwrapper.view.IFillViewProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class FillWrapper<T extends View, K> {

    public static final int NONE = 16843830, LOAD = 16843831, EMPTY = 16843832, ERROR = 16843833;
    private int mType = NONE;

    @IntDef({LOAD, EMPTY, ERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private K k;

    private IFillViewProvider<T, K> fillView;

    public IFillViewProvider<T, K> getFillView() {
        return fillView;
    }

    public void setFillView(IFillViewProvider<T, K> fillView) {
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
