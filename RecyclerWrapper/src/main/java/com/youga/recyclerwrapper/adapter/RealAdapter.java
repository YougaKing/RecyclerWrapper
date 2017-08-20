package com.youga.recyclerwrapper.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewGroup.LayoutParams;

import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;


/**
 * Created by Youga on 2015/9/2.
 */
public final class RealAdapter extends AdapterWrapper {

    private static final String TAG = "RealAdapter";
    private InteractionListener.InternalListener mListener;

    public RealAdapter(@NonNull RecyclerView.Adapter adapter, InteractionListener.InternalListener listener) {
        super(adapter);
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        if (mListener.getFillType() != FillWrapper.NONE) {
            return 1;
        } else {
            return mListener.getFootType() != FootWrapper.F_NONE ? super.getItemCount() + 1 : super.getItemCount();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mListener.getFillType() != FillWrapper.NONE) {
            return mListener.getFillType();
        } else if (position == super.getItemCount()) {
            return mListener.getFootType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FillWrapper.LOAD:
            case FillWrapper.EMPTY:
            case FillWrapper.ERROR:
                return new FillViewHolder(mListener.getFillView());
            case FootWrapper.F_LOAD:
            case FootWrapper.F_FAULT:
                return new FootViewHolder(mListener.getFootView());
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

    public void internalNotify() {
        getAdapter().notifyDataSetChanged();
    }


    private class FillViewHolder extends RecyclerView.ViewHolder {

        FillViewHolder(View itemView) {
            super(itemView);
        }

        void bindView() {
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

    private class FootViewHolder extends RecyclerView.ViewHolder {


        FootViewHolder(View itemView) {
            super(itemView);
        }

        void bindView() {
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
