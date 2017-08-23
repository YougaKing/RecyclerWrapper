package com.youga.recyclerwrapper.core;

import android.support.annotation.IntDef;
import android.view.View;

import com.youga.recyclerwrapper.view.FootView;
import com.youga.recyclerwrapper.view.IFootViewProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class FootWrapper<T extends View, K> {

    public static final int F_NONE = 16843834, F_LOAD = 16843835, F_FAULT = 16843836;
    private int mType = F_NONE;

    @IntDef({F_LOAD, F_FAULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private boolean isLoading;
    private K k;

    private IFootViewProvider<T, K> footView;

    public IFootViewProvider<T, K> getFootView() {
        return footView;
    }

    public void setFootView(IFootViewProvider<T, K> footView) {
        this.footView = footView;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public K getK() {
        return k;
    }

    public void setK(K k) {
        this.k = k;
    }
}
