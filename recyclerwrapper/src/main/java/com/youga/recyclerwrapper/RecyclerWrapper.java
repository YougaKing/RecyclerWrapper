package com.youga.recyclerwrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.youga.recyclerwrapper.core.Wrapper;
import com.youga.recyclerwrapper.view.FillView;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.LoadMoreWrapper;
import com.youga.recyclerwrapper.view.LoadMoreView;
import com.youga.recyclerwrapper.view.FillViewProvider;
import com.youga.recyclerwrapper.view.LoadMoreViewProvider;

/**
 * Created by Youga on 2017/8/17.
 */

public class RecyclerWrapper {

    private static RecyclerWrapper INSTANCE;
    FillViewProvider[] fillViewProviders = new FillViewProvider[2];
    LoadMoreViewProvider[] loadMoreViewProviders = new LoadMoreViewProvider[2];

    private RecyclerWrapper() {
    }

    static RecyclerWrapper getInstance() {
        if (INSTANCE == null) {
            synchronized (RecyclerWrapper.class) {
                if (INSTANCE == null) INSTANCE = new RecyclerWrapper();
            }
        }
        return INSTANCE;
    }

    public static Wrapper with(@NonNull final RecyclerView recyclerView) {
        if (recyclerView.getAdapter() == null) {
            throw new IllegalStateException("RecyclerView has no Adapter");
        }
        if (recyclerView.getLayoutManager() == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }
        initDefaultView(recyclerView.getContext().getApplicationContext());
        return new RecyclerViewInteraction(recyclerView);
    }

    public static Wrapper with(@NonNull final ListView listView) {
        if (listView.getAdapter() == null) {
            throw new IllegalStateException("ListView has no Adapter");
        }
        initDefaultView(listView.getContext().getApplicationContext());
        return new ListViewInteraction(listView);
    }

    public static <T extends View, K> void registerFillViewProvider(FillViewProvider<T, K> view) {
        getInstance().fillViewProviders[1] = view;
    }

    public static <T extends View, K> void registerFootViewProvider(LoadMoreViewProvider<T, K> view) {
        getInstance().loadMoreViewProviders[1] = view;
    }

    private static void initDefaultView(final Context context) {
        if (getInstance().fillViewProviders[0] == null) {
            getInstance().fillViewProviders[0] = new FillViewProvider<FillView, String>() {
                @Override
                public FillView createView() {
                    return new FillView(context);
                }

                @Override
                public void bindView(FillView fillView, String s, @FillWrapper.Type int type) {
                    if (type == FillWrapper.LOAD) {
                        fillView.showLoading();
                    } else if (type == FillWrapper.EMPTY) {
                        fillView.showEmpty();
                    } else {
                        fillView.showError();
                    }
                }
            };
        }

        if (getInstance().loadMoreViewProviders[0] == null) {
            getInstance().loadMoreViewProviders[0] = new LoadMoreViewProvider<LoadMoreView, String>() {
                @Override
                public LoadMoreView createView() {
                    return new LoadMoreView(context);
                }

                @Override
                public void bindView(LoadMoreView loadMoreView, String s, @LoadMoreWrapper.Type int type) {
                    if (type == LoadMoreWrapper.F_LOAD) {
                        loadMoreView.showLoading();
                    } else {
                        loadMoreView.showFault();
                    }
                }
            };
        }
    }
}
