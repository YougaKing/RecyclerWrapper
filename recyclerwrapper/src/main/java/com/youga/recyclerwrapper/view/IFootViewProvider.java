package com.youga.recyclerwrapper.view;

import android.view.View;

import com.youga.recyclerwrapper.core.FootWrapper;

/**
 * Created by Youga on 2017/8/18.
 */

public interface IFootViewProvider<T extends View, K> {

    T createView();

    void bindView(T t, K k, @FootWrapper.Type int type);
}
