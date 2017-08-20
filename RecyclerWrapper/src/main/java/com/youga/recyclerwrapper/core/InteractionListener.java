package com.youga.recyclerwrapper.core;

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
    }

    interface RevealListener {

        void showLoadView();

        void showErrorView();

        void showEmptyView();

        void showItemView();

        void loadMoreFault();

        void loadMoreEnable();

        void loadMoreNone();
    }
}
