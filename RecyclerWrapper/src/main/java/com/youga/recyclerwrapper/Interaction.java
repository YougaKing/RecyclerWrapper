package com.youga.recyclerwrapper;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.youga.recyclerwrapper.core.InteractionListener;
import com.youga.recyclerwrapper.adapter.RealAdapter;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;
import com.youga.recyclerwrapper.core.Wrapper;
import com.youga.recyclerwrapper.view.IFillViewProvider;
import com.youga.recyclerwrapper.view.IFootViewProvider;

/**
 * Created by Youga on 2017/8/17.
 */

public class Interaction implements Wrapper, InteractionListener.RevealListener, InteractionListener.InternalListener {

    private FillWrapper fillWrapper = new FillWrapper<>();
    private FootWrapper footWrapper = new FootWrapper<>();
    public RealAdapter realAdapter;
    private Wrapper wrapper;
    private RecyclerView recyclerView;
    private LoadMoreListener mLoadMoreListener;

    public Interaction(final RecyclerView recyclerView) {
        wrapper = this;
        realAdapter = new RealAdapter(recyclerView.getAdapter(), this);
        this.recyclerView = recyclerView;
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                RecyclerWrapper.getInstance().listener = null;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });
    }

    @Override
    public <K> void showLoadView(K k) {
        fillWrapper.setK(k);
        fillWrapper.setType(FillWrapper.LOAD);
        realAdapter.internalNotify();
    }

    @Override
    public <K> void showErrorView(K k) {
        fillWrapper.setK(k);
        fillWrapper.setType(FillWrapper.ERROR);
        realAdapter.internalNotify();
    }

    @Override
    public <K> void showEmptyView(K k) {
        fillWrapper.setK(k);
        fillWrapper.setType(FillWrapper.EMPTY);
        realAdapter.internalNotify();
    }

    @Override
    public void showItemView() {
        footWrapper.setLoading(false);
        fillWrapper.setType(FillWrapper.NONE);
        realAdapter.internalNotify();
    }

    @Override
    public <K> void loadMoreFault(K k) {
        footWrapper.setK(k);
        footWrapper.setType(FootWrapper.F_FAULT);
        realAdapter.internalNotify();
    }

    @Override
    public <K> void loadMoreEnable(K k) {
        footWrapper.setK(k);
        footWrapper.setType(FootWrapper.F_LOAD);
        realAdapter.internalNotify();
    }

    @Override
    public void loadMoreNone() {
        footWrapper.setType(FootWrapper.F_NONE);
        realAdapter.internalNotify();
    }

    @Override
    public int getFillType() {
        return fillWrapper.getType();
    }

    @Override
    public int getFootType() {
        return footWrapper.getType();
    }

    @Override
    public View getFillView() {
        return fillWrapper.getFillView() == null ? RecyclerWrapper.getInstance().fillViewProviders[0].createView()
                : fillWrapper.getFillView().createView();
    }

    @Override
    public View getFootView() {
        return footWrapper.getFootView() == null ? RecyclerWrapper.getInstance().footViewProviders[0].createView()
                : footWrapper.getFootView().createView();
    }

    @Override
    public int getWidth() {
        return recyclerView.getWidth();
    }

    @Override
    public int getHeight() {
        return recyclerView.getHeight();
    }

    @Override
    public RecyclerView.LayoutManager getLayoutManager() {
        return recyclerView.getLayoutManager();
    }

    @Override
    public void bindFillView(View view) {
        switch (fillWrapper.getType()) {
            case FillWrapper.LOAD:
                fillWrapper.getFillView().bindView(view, fillWrapper.getK(), FillWrapper.LOAD);
                break;
            case FillWrapper.EMPTY:
                fillWrapper.getFillView().bindView(view, fillWrapper.getK(), FillWrapper.EMPTY);
                break;
            case FillWrapper.ERROR:
                fillWrapper.getFillView().bindView(view, fillWrapper.getK(), FillWrapper.ERROR);
                break;
        }
    }

    @Override
    public void bindFootView(View view, int position) {
        switch (footWrapper.getType()) {
            case FootWrapper.F_LOAD:
                if (!footWrapper.isLoading()) {
                    footWrapper.setLoading(true);
                    if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore(position);
                }
                footWrapper.getFootView().bindView(view, footWrapper.getK(), FootWrapper.F_LOAD);
                break;
            case FootWrapper.F_FAULT:
                footWrapper.getFootView().bindView(view, footWrapper.getK(), FootWrapper.F_FAULT);
                break;
        }
    }

    @Override
    public void footViewClick(View view, int position) {
        if (footWrapper.getType() != FootWrapper.F_FAULT || footWrapper.isLoading())
            return;
        footWrapper.setType(FootWrapper.F_LOAD);
        footWrapper.setLoading(true);
        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore(position);
        footWrapper.getFootView().bindView(view, null, FootWrapper.F_LOAD);
        realAdapter.internalNotify();
    }

    @Override
    public boolean loadMoreUnavailable() {
        return mLoadMoreListener == null;
    }

    public Wrapper getWrapper() {
        return wrapper;
    }

    @Override
    public <T extends View, K> Wrapper fillView(IFillViewProvider<T, K> view) {
        fillWrapper.setFillView(view);
        return wrapper;
    }

    @Override
    public <T extends View, K> Wrapper footView(IFootViewProvider<T, K> view) {
        footWrapper.setFootView(view);
        return wrapper;
    }

    @Override
    public void wrapper(LoadMoreListener listener) {
        mLoadMoreListener = listener;
        recyclerView.setAdapter(realAdapter);
    }
}
