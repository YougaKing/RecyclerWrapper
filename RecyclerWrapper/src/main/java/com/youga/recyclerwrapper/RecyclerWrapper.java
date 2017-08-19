package com.youga.recyclerwrapper;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;

import com.youga.recyclerwrapper.adapter.RealAdapter;
import com.youga.recyclerwrapper.view.FillView;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;
import com.youga.recyclerwrapper.view.FootView;
import com.youga.recyclerwrapper.view.IFillViewProvider;
import com.youga.recyclerwrapper.view.IFootViewProvider;


/**
 * Created by Youga on 2017/8/17.
 */

public class RecyclerWrapper {

    private Wrapper mWrapper;
    private static RecyclerWrapper mInstance;
    private IFillViewProvider[] mFillViewProviders = new IFillViewProvider[2];
    private IFootViewProvider[] mFootViewProviders = new IFootViewProvider[2];

    private RecyclerWrapper() {
    }

    private static RecyclerWrapper getInstance() {
        if (mInstance == null) {
            synchronized (RecyclerWrapper.class) {
                if (mInstance == null) mInstance = new RecyclerWrapper();
            }
        }
        return mInstance;
    }


    public static RecyclerWrapper with(@NonNull final RecyclerView recyclerView) {
        if (recyclerView.getAdapter() == null) {
            throw new IllegalStateException("RecyclerView has no Adapter");
        }
        if (recyclerView.getLayoutManager() == null) {
            throw new IllegalStateException("RecyclerView has no LayoutManager");
        }

        initDefaultView(recyclerView.getContext().getApplicationContext());

        final Wrapper wrapper = new Wrapper();
        wrapper.adapter = recyclerView.getAdapter();
        wrapper.layoutManager = recyclerView.getLayoutManager();
        wrapper.metrics = recyclerView.getResources().getDisplayMetrics();

        wrapper.fillWrapper = new FillWrapper<>(getInstance().mFillViewProviders[1] == null ? getInstance().mFillViewProviders[0] : getInstance().mFillViewProviders[1]);
        wrapper.footWrapper = new FootWrapper<>(getInstance().mFootViewProviders[1] == null ? getInstance().mFootViewProviders[0] : getInstance().mFootViewProviders[1]);

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (wrapper.width == 0) {
                    wrapper.width = recyclerView.getWidth();
                    wrapper.height = recyclerView.getHeight();
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        recyclerView.setAdapter(new RealAdapter(wrapper));
        getInstance().mWrapper = wrapper;
        return mInstance;
    }

    private static void initDefaultView(final Context context) {
        if (getInstance().mFillViewProviders[0] == null) {
            getInstance().mFillViewProviders[0] = new IFillViewProvider<FillView, String>() {
                @Override
                public FillView createView() {
                    return new FillView(context);
                }

                @Override
                public void bindView(FillView fillView, String s, @FillWrapper.Type int type) {
                    if (type == FillWrapper.LOAD) {
                        fillView.showLoading();
                    } else if (type == FillWrapper.EMPTY) {
                        fillView.showTips(s);
                    } else {
                        fillView.showTips(s);
                    }
                }
            };
        }

        if (getInstance().mFootViewProviders[0] == null) {
            getInstance().mFootViewProviders[0] = new IFootViewProvider<FootView, String>() {
                @Override
                public FootView createView() {
                    return new FootView(context);
                }

                @Override
                public void bindView(FootView footView, String s, @FootWrapper.Type int type) {
                    if (type == FootWrapper.F_LOAD) {
                        footView.showLoading();
                    } else {
                        footView.showFault(s);
                    }
                }
            };
        }
    }

    public <T extends View, K> RecyclerWrapper fillView(IFillViewProvider<T, K> view) {
        mWrapper.fillWrapper.setFillView(view);
        return this;
    }

    public <T extends View, K> RecyclerWrapper footView(IFootViewProvider<T, K> view) {
        mWrapper.footWrapper.setFootView(view);
        return this;
    }

    public void wrapper(LoadMoreListener listener) {
        mWrapper.loadMoreListener = listener;
    }


    public static void showLoadView() {
        if (mInstance.mWrapper == null) return;
        mInstance.mWrapper.interactionListener.showLoadView();
    }

    public static void showErrorView() {
        if (mInstance.mWrapper == null) return;
        mInstance.mWrapper.interactionListener.showErrorView();
    }

    public static void showEmptyView() {
        if (mInstance.mWrapper == null) return;
        mInstance.mWrapper.interactionListener.showEmptyView();
    }

    public static void loadMoreFault() {
        if (mInstance.mWrapper == null) return;
        mInstance.mWrapper.interactionListener.loadMoreFault();
    }

    public static void showItemView() {
        if (mInstance.mWrapper == null) return;
        mInstance.mWrapper.interactionListener.showItemView();
    }

    public static void haveMore(boolean haveMore) {
        if (mInstance.mWrapper == null) return;
        if (haveMore) {
            mInstance.mWrapper.footWrapper.setType(FootWrapper.F_LOAD);
        } else {
            mInstance.mWrapper.footWrapper.setType(FootWrapper.F_NONE);
        }
        showItemView();
    }

    public static <T extends View, K> void registerFillViewProvider(IFillViewProvider<T, K> view) {
        getInstance().mFillViewProviders[1] = view;
    }

    public static <T extends View, K> void registerFootViewProvider(IFootViewProvider<T, K> view) {
        getInstance().mFootViewProviders[1] = view;
    }
}
