package com.youga.recyclerwrapper.view;

import android.view.View;

import com.youga.recyclerwrapper.core.LoadMoreWrapper;

/**
 * Created by Youga on 2017/8/18.
 */

public interface LoadMoreViewProvider<T extends View, K> {

    T createView();

    void bindView(T t, K k, @LoadMoreWrapper.Type int type);
}
