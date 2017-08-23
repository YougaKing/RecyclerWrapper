package com.youga.recyclerwrapper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youga.recyclerwrapper.core.InteractionListener;
import com.youga.recyclerwrapper.core.Wrapper;
import com.youga.recyclerwrapper.view.FillView;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;
import com.youga.recyclerwrapper.view.FootView;
import com.youga.recyclerwrapper.view.IFillViewProvider;
import com.youga.recyclerwrapper.view.IFootViewProvider;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Youga on 2017/8/17.
 */

public class RecyclerWrapper {

    private static RecyclerWrapper INSTANCE;
    IFillViewProvider[] fillViewProviders = new IFillViewProvider[2];
    IFootViewProvider[] footViewProviders = new IFootViewProvider[2];
    InteractionListener.RevealListener listener;

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
        Interaction interaction = new Interaction(recyclerView);
        return interaction.getWrapper();
    }

    public static <T extends View, K> void registerFillViewProvider(IFillViewProvider<T, K> view) {
        getInstance().fillViewProviders[1] = view;
    }

    public static <T extends View, K> void registerFootViewProvider(IFootViewProvider<T, K> view) {
        getInstance().footViewProviders[1] = view;
    }

    private static void initDefaultView(final Context context) {
        if (getInstance().fillViewProviders[0] == null) {
            getInstance().fillViewProviders[0] = new IFillViewProvider<FillView, String>() {
                @Override
                public FillView createView() {
                    return new FillView(context);
                }

                @Override
                public void bindView(FillView fillView, String s, @FillWrapper.Type int type) {
                    if (type == FillWrapper.LOAD) {
                        fillView.showLoading();
                    } else if (type == FillWrapper.EMPTY) {
                        fillView.showEmpty("什么都没有");
                    } else {
                        fillView.showError("网络出现问题了");
                    }
                }
            };
        }

        if (getInstance().footViewProviders[0] == null) {
            getInstance().footViewProviders[0] = new IFootViewProvider<FootView, String>() {
                @Override
                public FootView createView() {
                    return new FootView(context);
                }

                @Override
                public void bindView(FootView footView, String s, @FootWrapper.Type int type) {
                    if (type == FootWrapper.F_LOAD) {
                        footView.showLoading();
                    } else {
                        footView.showFault("点击加载更多...");
                    }
                }
            };
        }
    }
}
