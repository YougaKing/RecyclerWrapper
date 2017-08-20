package com.youga.recyclerwrapper;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;

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

    private int width;
    private int height;
    private RecyclerView.LayoutManager layoutManager;
    private FillWrapper fillWrapper;
    private FootWrapper footWrapper;


    public RealAdapter realAdapter;
    private Wrapper wrapper;
    private RecyclerView recyclerView;

    public Interaction(final RecyclerView recyclerView) {
        wrapper = this;
        realAdapter = new RealAdapter(recyclerView.getAdapter(), this);
        layoutManager = recyclerView.getLayoutManager();
        this.recyclerView = recyclerView;
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (width == 0) {
                    width = (recyclerView.getWidth());
                    height = (recyclerView.getHeight());
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
        recyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                RecyclerWrapper.getInstance().listener = null;
            }

            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });

        IFillViewProvider[] fillViewProviders = RecyclerWrapper.getInstance().fillViewProviders;
        IFootViewProvider[] footViewProviders = RecyclerWrapper.getInstance().footViewProviders;
        fillWrapper = new FillWrapper<>(fillViewProviders[1] == null ? fillViewProviders[0] : fillViewProviders[1]);
        footWrapper = new FootWrapper<>(footViewProviders[1] == null ? footViewProviders[0] : footViewProviders[1]);
    }


    @Override
    public void showLoadView() {
        fillWrapper.setType(FillWrapper.LOAD);
        realAdapter.internalNotify();
    }

    @Override
    public void showErrorView() {
        fillWrapper.setType(FillWrapper.ERROR);
        realAdapter.internalNotify();
    }

    @Override
    public void showEmptyView() {
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
    public void loadMoreFault() {
        footWrapper.setType(FootWrapper.F_FAULT);
        realAdapter.internalNotify();
    }

    @Override
    public void loadMoreEnable() {
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
        recyclerView.setAdapter(realAdapter);
    }
}
