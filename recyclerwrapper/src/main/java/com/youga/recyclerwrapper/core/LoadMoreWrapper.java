package com.youga.recyclerwrapper.core;

import android.support.annotation.IntDef;
import android.view.View;

import com.youga.recyclerwrapper.view.LoadMoreViewProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class LoadMoreWrapper<T extends View, K> {

    public static final int F_NONE = FillWrapper.ERROR + 1, F_LOAD = F_NONE + 1, F_FAULT = F_LOAD + 1;
    private int mType = F_NONE;

    @IntDef({F_LOAD, F_FAULT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {

    }

    private boolean isLoading;
    private K k;

    private LoadMoreViewProvider<T, K> loadMoreView;

    public LoadMoreViewProvider<T, K> getLoadMoreView() {
        return loadMoreView;
    }

    public void setLoadMoreView(LoadMoreViewProvider<T, K> loadMoreView) {
        this.loadMoreView = loadMoreView;
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
