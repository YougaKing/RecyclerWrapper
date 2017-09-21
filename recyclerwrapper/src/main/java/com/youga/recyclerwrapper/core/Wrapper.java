package com.youga.recyclerwrapper.core;

import android.view.View;

import com.youga.recyclerwrapper.LoadMoreListener;
import com.youga.recyclerwrapper.view.FillViewProvider;
import com.youga.recyclerwrapper.view.ItemViewProvider;
import com.youga.recyclerwrapper.view.LoadMoreViewProvider;

/**
 * Created by Youga on 2017/8/20.
 */

public interface Wrapper {

    <T extends View, K> Wrapper fillView(FillViewProvider<T, K> view);

    <T extends View, K> Wrapper loadMoreView(LoadMoreViewProvider<T, K> view);
//
//    Wrapper addHeaderView(ItemViewProvider viewProvider);
//
//    Wrapper addFooterView(ItemViewProvider viewProvider);

    InteractionListener.RevealListener wrapper(LoadMoreListener listener);
}
