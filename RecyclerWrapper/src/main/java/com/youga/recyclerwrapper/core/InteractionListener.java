package com.youga.recyclerwrapper.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Youga on 2017/8/17.
 */

public interface InteractionListener {

    interface InternalListener {

        int getFillType();

        int getFootType();

        View getFillView();

        View getFootView();

        int getWidth();

        int getHeight();

        RecyclerView.LayoutManager getLayoutManager();

        void bindFillView(View view);

        void bindFootView(View view, int position);

        void footViewClick(View view, int position);

        boolean loadMoreUnavailable();
    }

    interface RevealListener {

        <K> void showLoadView(K k);

        <K> void showErrorView(K k);

        <K> void showEmptyView(K k);

        void showItemView();

        <K> void loadMoreFault(K k);

        <K> void loadMoreEnable(K k);

        void loadMoreNone();
    }
}
