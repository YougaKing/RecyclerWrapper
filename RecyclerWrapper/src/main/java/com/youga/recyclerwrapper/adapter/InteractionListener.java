package com.youga.recyclerwrapper.adapter;

/**
 * Created by Youga on 2017/8/17.
 */

public interface InteractionListener {

    void showLoadView();

    void showErrorView();

    void showEmptyView();

    void loadMoreFault();

    void showItemView();
}
