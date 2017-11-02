package com.youga.recyclerwrapper.listview;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;
import com.youga.recyclerwrapper.core.LoadMoreWrapper;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/11/2 0002-14:08
 */

public class RealAdapter extends AdapterWrapper {

    private final InteractionListener.InternalListener mListener;

    public RealAdapter(BaseAdapter adapter, InteractionListener.InternalListener listener) {
        super(adapter);
        mListener = listener;
    }

    @Override
    public int getCount() {
        if (mListener.getFillType() != FillWrapper.NONE) {
            return 1;
        } else {
            if (mListener.loadMoreUnavailable()) {
                return super.getCount();
            } else {
                return mListener.getLoadMoreType() != LoadMoreWrapper.F_NONE ? super.getCount() + 1 : super.getCount();
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mListener.getFillType() != FillWrapper.NONE) {
            return mListener.getFillType();
        } else if (position == super.getCount()) {
            return mListener.getLoadMoreType();
        } else {
            return super.getItemViewType(position);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int viewType = this.getItemViewType(position);
        switch (viewType) {
            case FillWrapper.LOAD:
            case FillWrapper.EMPTY:
            case FillWrapper.ERROR:
                return new RealAdapter.FillViewHolder(mListener.getFillView()).getView();
            case LoadMoreWrapper.F_LOAD:
            case LoadMoreWrapper.F_FAULT:
                return new RealAdapter.FootViewHolder(mListener.getLoadMoreView()).getView();
            default:
                return super.getView(position, convertView, parent);
        }
    }

    private class FillViewHolder extends ViewHolder {

        FillViewHolder(final View itemView) {
            super(itemView);
            if (mListener.getWidth() == 0) {
                itemView.setVisibility(View.INVISIBLE);
                itemView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (itemView.getLayoutParams() == null) {
                            itemView.setLayoutParams(new ViewGroup.LayoutParams(mListener.getWidth(), mListener.getHeight()));
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
                    itemView.setLayoutParams(new ViewGroup.LayoutParams(mListener.getWidth(), mListener.getHeight()));
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

    private class FootViewHolder extends ViewHolder {

        FootViewHolder(final View itemView) {
            super(itemView);

            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, itemView.getResources().getDisplayMetrics());
            if (itemView.getLayoutParams() == null) {
                itemView.setLayoutParams(new ViewGroup.LayoutParams(mListener.getWidth(), height));
            } else {
                itemView.getLayoutParams().width = mListener.getWidth();
                itemView.getLayoutParams().height = height;
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.loadMoreViewClick(itemView, getAdapter().getCount());
                }
            });
            mListener.bindLoadMoreView(itemView, getAdapter().getCount());
        }
    }

    public void internalNotify() {
        getAdapter().notifyDataSetChanged();
    }
}
