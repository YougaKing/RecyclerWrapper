package com.youga.recyclerwrapper.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import android.view.ViewGroup.LayoutParams;

import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.ItemViewTypeProvider;
import com.youga.recyclerwrapper.core.LoadMoreWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;


/**
 * Created by Youga on 2015/9/2.
 */
public final class RealAdapter extends AdapterWrapper {

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
            if (mListener.loadMoreUnavailable()) {
                return super.getItemCount();
            } else {
                return mListener.getLoadMoreType() != LoadMoreWrapper.F_NONE ? super.getItemCount() + 1 : super.getItemCount();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mListener.getFillType() != FillWrapper.NONE) {
            return mListener.getFillType();
        } else if (position == super.getItemCount()) {
            return mListener.getLoadMoreType();
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
            case LoadMoreWrapper.F_LOAD:
            case LoadMoreWrapper.F_FAULT:
                return new FootViewHolder(mListener.getLoadMoreView());
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
        } else if (holder instanceof ItemViewTypeProvider.ItemViewHolder) {
            ItemViewTypeProvider.ItemViewHolder viewHolder = (ItemViewTypeProvider.ItemViewHolder) holder;
            viewHolder.bindData(position);
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
            if (mListener.getWidth() == 0) {
                itemView.setVisibility(View.INVISIBLE);
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemView.getLayoutParams() == null) {
                            itemView.setLayoutParams(new LayoutParams(mListener.getWidth(), mListener.getHeight()));
                        } else {
                            itemView.getLayoutParams().width = mListener.getWidth();
                            itemView.getLayoutParams().height = mListener.getHeight();
                        }
                        itemView.setVisibility(View.VISIBLE);
                        itemView.requestLayout();
                    }
                }, 500);
            } else {
                if (itemView.getLayoutParams() == null) {
                    itemView.setLayoutParams(new LayoutParams(mListener.getWidth(), mListener.getHeight()));
                } else {
                    itemView.getLayoutParams().width = mListener.getWidth();
                    itemView.getLayoutParams().height = mListener.getHeight();
                }
                itemView.setVisibility(View.VISIBLE);
                itemView.requestLayout();
                itemView.setVisibility(View.VISIBLE);
            }
            mListener.bindFillView(itemView);
        }
    }

    private class FootViewHolder extends RecyclerView.ViewHolder {

        FootViewHolder(View itemView) {
            super(itemView);
        }

        void bindView() {
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, itemView.getResources().getDisplayMetrics());
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new LayoutParams(mListener.getWidth(), height));
            } else {
                itemView.getLayoutParams().width = mListener.getWidth();
                itemView.getLayoutParams().height = height;
            }
            if (mListener.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(mListener.getWidth(), height);
                params.setFullSpan(true);
                itemView.setLayoutParams(params);
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.loadMoreViewClick(itemView, getAdapter().getItemCount());
                }
            });
            mListener.bindLoadMoreView(itemView, getAdapter().getItemCount());
        }
    }
}
