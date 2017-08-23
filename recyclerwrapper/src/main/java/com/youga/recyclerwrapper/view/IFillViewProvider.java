package com.youga.recyclerwrapper.view;

import android.view.View;

import com.youga.recyclerwrapper.core.FillWrapper;

/**
 * Created by Youga on 2017/8/18.
 */

public interface IFillViewProvider<T extends View, K> {

    T createView();

    void bindView(T t, K k, @FillWrapper.Type int type);
}
