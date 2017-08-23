package com.youga.recyclerwrapper.core;

import android.view.View;

import com.youga.recyclerwrapper.LoadMoreListener;
import com.youga.recyclerwrapper.view.IFillViewProvider;
import com.youga.recyclerwrapper.view.IFootViewProvider;

/**
 * Created by Youga on 2017/8/20.
 */

public interface Wrapper {

    <T extends View, K> Wrapper fillView(IFillViewProvider<T, K> view);

    <T extends View, K> Wrapper footView(IFootViewProvider<T, K> view);

    InteractionListener.RevealListener wrapper(LoadMoreListener listener);
}
