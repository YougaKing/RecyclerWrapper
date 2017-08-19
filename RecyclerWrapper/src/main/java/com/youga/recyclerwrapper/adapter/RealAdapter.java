package com.youga.recyclerwrapper.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewGroup.LayoutParams;

import com.youga.recyclerwrapper.Wrapper;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;


/**
 * Created by Youga on 2015/9/2.
 */
public class RealAdapter extends AdapterWrapper implements InteractionListener {

    private static final String TAG = "RealAdapter";
    private Wrapper mWrapper;

    public RealAdapter(@NonNull Wrapper wrapper) {
        super(wrapper.adapter);
        mWrapper = wrapper;
        mWrapper.interactionListener = this;
    }

    @Override
    public int getItemCount() {
        if (mWrapper.fillWrapper.getType() != FillWrapper.NONE) {
            return 1;
        } else {
            if (mWrapper.loadMoreListener == null) {
                return super.getItemCount();
            } else {
                return mWrapper.footWrapper.getType() != FootWrapper.F_NONE ? super.getItemCount() + 1 : super.getItemCount();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mWrapper.fillWrapper.getType() != FillWrapper.NONE) {
            return mWrapper.fillWrapper.getType();
        } else if (position == super.getItemCount()) {
            return mWrapper.footWrapper.getType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder()-->viewType:" + viewType);
        switch (viewType) {
            case FillWrapper.LOAD:
            case FillWrapper.EMPTY:
            case FillWrapper.ERROR:
                return new FillViewHolder(mWrapper.fillWrapper.getFillView().createView());
            case FootWrapper.F_LOAD:
            case FootWrapper.F_FAULT:
                return new FootViewHolder(mWrapper.footWrapper.getFootView().createView());
            default:
                return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof FillViewHolder) {
            FillViewHolder fillHolder = (FillViewHolder) holder;
            fillHolder.bindView();
        } else if (holder instanceof FootViewHolder) {
            FootViewHolder footHolder = (FootViewHolder) holder;
            footHolder.bindView();
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    @Override
    public void showLoadView() {
        mWrapper.fillWrapper.setType(FillWrapper.LOAD);
        getAdapter().notifyDataSetChanged();
        Log.w(TAG, "showLoadView()");
    }

    @Override
    public void showErrorView() {
        mWrapper.fillWrapper.setType(FillWrapper.ERROR);
        getAdapter().notifyDataSetChanged();
        Log.w(TAG, "showErrorView()");
    }

    @Override
    public void showEmptyView() {
        mWrapper.fillWrapper.setType(FillWrapper.EMPTY);
        getAdapter().notifyDataSetChanged();
        Log.w(TAG, "showEmptyView()");
    }

    @Override
    public void loadMoreFault() {
        mWrapper.footWrapper.setType(FootWrapper.F_FAULT);
        getAdapter().notifyDataSetChanged();
        Log.w(TAG, "loadMoreFault()");
    }

    @Override
    public void showItemView() {
        mWrapper.footWrapper.setLoading(false);
        mWrapper.fillWrapper.setType(FillWrapper.NONE);
        getAdapter().notifyDataSetChanged();
        Log.w(TAG, "showItemView()");
    }

    public class FillViewHolder extends RecyclerView.ViewHolder {

        public FillViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView() {
            if (mWrapper.width == 0) {
                itemView.setVisibility(View.INVISIBLE);
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemView.getLayoutParams() == null) {
                            itemView.setLayoutParams(new LayoutParams(mWrapper.width, mWrapper.height));
                        } else {
                            itemView.getLayoutParams().width = mWrapper.width;
                            itemView.getLayoutParams().height = mWrapper.height;
                        }
                        itemView.setVisibility(View.VISIBLE);
                        itemView.requestLayout();
                    }
                }, 500);
            } else {
                itemView.setVisibility(View.VISIBLE);
            }

            switch (mWrapper.fillWrapper.getType()) {
                case FillWrapper.LOAD:
                    mWrapper.fillWrapper.getFillView().bindView(itemView, null, FillWrapper.LOAD);
                    break;
                case FillWrapper.EMPTY:
                    mWrapper.fillWrapper.getFillView().bindView(itemView, null, FillWrapper.EMPTY);
                    break;
                case FillWrapper.ERROR:
                    mWrapper.fillWrapper.getFillView().bindView(itemView, null, FillWrapper.ERROR);
                    break;
            }
        }
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {


        public FootViewHolder(View itemView) {
            super(itemView);
        }

        public void bindView() {
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new LayoutParams(mWrapper.width, LayoutParams.WRAP_CONTENT));
            } else {
                itemView.getLayoutParams().width = mWrapper.width;
            }
            if (mWrapper.layoutManager instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(mWrapper.width, mWrapper.height);
                params.setFullSpan(true);
                itemView.setLayoutParams(params);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWrapper.footWrapper.getType() != FootWrapper.F_FAULT || mWrapper.footWrapper.isLoading())
                        return;
                    mWrapper.footWrapper.setType(FootWrapper.F_LOAD);
                    mWrapper.footWrapper.setLoading(true);
                    mWrapper.loadMoreListener.onLoadMore(getAdapter().getItemCount());
                    mWrapper.footWrapper.getFootView().bindView(itemView, null, FootWrapper.F_LOAD);
                    getAdapter().notifyDataSetChanged();
                }
            });

            switch (mWrapper.footWrapper.getType()) {
                case FootWrapper.F_LOAD:
                    if (!mWrapper.footWrapper.isLoading()) {
                        mWrapper.footWrapper.setLoading(true);
                        mWrapper.loadMoreListener.onLoadMore(getAdapter().getItemCount());
                    }
                    mWrapper.footWrapper.getFootView().bindView(itemView, null, FootWrapper.F_LOAD);
                    break;
                case FootWrapper.F_FAULT:
                    mWrapper.footWrapper.getFootView().bindView(itemView, null, FootWrapper.F_FAULT);
                    break;
            }
        }
    }
}
