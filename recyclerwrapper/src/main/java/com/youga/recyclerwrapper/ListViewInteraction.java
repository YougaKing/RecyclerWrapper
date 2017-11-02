package com.youga.recyclerwrapper;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;
import com.youga.recyclerwrapper.core.LoadMoreWrapper;
import com.youga.recyclerwrapper.core.Wrapper;
import com.youga.recyclerwrapper.listview.RealAdapter;
import com.youga.recyclerwrapper.view.FillViewProvider;
import com.youga.recyclerwrapper.view.LoadMoreViewProvider;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/11/2 0002-13:56
 */

public class ListViewInteraction implements Wrapper {

    private final ListView listView;
    private RealAdapter realAdapter;
    private FillWrapper fillWrapper = new FillWrapper<>();
    private LoadMoreWrapper loadMoreWrapper = new LoadMoreWrapper<>();
    private LoadMoreListener mLoadMoreListener;

    public ListViewInteraction(ListView listView) {
        Adapter adapter = listView.getAdapter();
        if (adapter instanceof BaseAdapter) {
            realAdapter = new RealAdapter((BaseAdapter) adapter, mInternalListener);
        } else {
            throw new IllegalStateException("listView.getAdapter must instanceof BaseAdapter");
        }
        this.listView = listView;

        FillViewProvider[] fillViewProviders = RecyclerWrapper.getInstance().fillViewProviders;
        LoadMoreViewProvider[] footViewProviders = RecyclerWrapper.getInstance().loadMoreViewProviders;
        fillWrapper.setFillView(fillViewProviders[1] == null ? fillViewProviders[0] : fillViewProviders[1]);
        loadMoreWrapper.setLoadMoreView(footViewProviders[1] == null ? footViewProviders[0] : footViewProviders[1]);
    }


    @Override
    public <T extends View, K> Wrapper fillView(FillViewProvider<T, K> view) {
        fillWrapper.setFillView(view);
        return this;
    }

    @Override
    public <T extends View, K> Wrapper loadMoreView(LoadMoreViewProvider<T, K> view) {
        loadMoreWrapper.setLoadMoreView(view);
        return this;
    }

    @Override
    public InteractionListener.RevealListener wrapper(LoadMoreListener listener) {
        mLoadMoreListener = listener;
        listView.setAdapter(realAdapter);
        return mRevealListener;
    }

    private InteractionListener.RevealListener mRevealListener = new InteractionListener.RevealListener() {

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
            loadMoreWrapper.setLoading(false);
            fillWrapper.setType(FillWrapper.NONE);
            realAdapter.internalNotify();
        }

        @Override
        public <K> void loadMoreFault(K k) {
            loadMoreWrapper.setK(k);
            loadMoreWrapper.setType(LoadMoreWrapper.F_FAULT);
            realAdapter.internalNotify();
        }

        @Override
        public void decideMore(boolean more) {
            if (more) {
                loadMoreWrapper.setType(LoadMoreWrapper.F_LOAD);
                realAdapter.internalNotify();
            } else {
                loadMoreWrapper.setType(LoadMoreWrapper.F_NONE);
                realAdapter.internalNotify();
            }
            showItemView();
        }
    };


    private InteractionListener.InternalListener mInternalListener = new InteractionListener.InternalListener() {

        @Override
        public int getFillType() {
            return fillWrapper.getType();
        }

        @Override
        public int getLoadMoreType() {
            return loadMoreWrapper.getType();
        }

        @Override
        public View getFillView() {
            return fillWrapper.getFillView().createView();
        }

        @Override
        public View getLoadMoreView() {
            return loadMoreWrapper.getLoadMoreView().createView();
        }

        @Override
        public int getWidth() {
            return listView.getWidth();
        }

        @Override
        public int getHeight() {
            return listView.getHeight();
        }

        @Override
        public RecyclerView.LayoutManager getLayoutManager() {
            return null;
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
        public void bindLoadMoreView(View view, int position) {
            switch (loadMoreWrapper.getType()) {
                case LoadMoreWrapper.F_LOAD:
                    loadMoreWrapper.getLoadMoreView().bindView(view, loadMoreWrapper.getK(), LoadMoreWrapper.F_LOAD);
                    if (!loadMoreWrapper.isLoading()) {
                        loadMoreWrapper.setLoading(true);
                        if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore(position);
                    }
                    break;
                case LoadMoreWrapper.F_FAULT:
                    loadMoreWrapper.getLoadMoreView().bindView(view, loadMoreWrapper.getK(), LoadMoreWrapper.F_FAULT);
                    break;
            }
        }

        @Override
        public void loadMoreViewClick(View view, int position) {
            if (loadMoreWrapper.getType() != LoadMoreWrapper.F_FAULT || loadMoreWrapper.isLoading())
                return;
            loadMoreWrapper.setType(LoadMoreWrapper.F_LOAD);
            loadMoreWrapper.setLoading(true);
            loadMoreWrapper.getLoadMoreView().bindView(view, null, LoadMoreWrapper.F_LOAD);
            realAdapter.internalNotify();
            if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore(position);
        }

        @Override
        public boolean loadMoreUnavailable() {
            return mLoadMoreListener == null;
        }
    };
}
