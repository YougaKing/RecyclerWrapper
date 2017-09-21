package com.youga.recyclerwrapper.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youga.recyclerwrapper.view.ItemViewProvider;

import java.util.TreeMap;

/**
 * Created by Youga on 2017/8/17.
 */

public interface InteractionListener {

    interface InternalListener {

        int getFillType();

        int getLoadMoreType();

        View getFillView();

        View getLoadMoreView();

        int getWidth();

        int getHeight();

        RecyclerView.LayoutManager getLayoutManager();

        void bindFillView(View view);

        void bindLoadMoreView(View view, int position);

        void loadMoreViewClick(View view, int position);

        boolean loadMoreUnavailable();
    }

    interface RevealListener {

        <K> void showLoadView(K k);

        <K> void showErrorView(K k);

        <K> void showEmptyView(K k);

        void showItemView();

        <K> void loadMoreFault(K k);

        void decideMore(boolean more);
    }
}
