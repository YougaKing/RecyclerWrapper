package com.youga.recyclerwrapper.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.youga.recyclerwrapper.view.ItemViewProvider;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/9/21 0021-9:59
 */

public final class ItemViewTypeProvider implements ItemViewProvider {

    public static final int HEADER = LoadMoreWrapper.F_FAULT + 1, FOOTER = HEADER + 1;

    private ItemViewProvider viewProvider;
    private int viewType;

    public ItemViewTypeProvider(ItemViewProvider viewProvider, int viewType) {
        this.viewProvider = viewProvider;
        this.viewType = viewType;
    }

    public RecyclerView.ViewHolder createViewHolder(ViewGroup parent) {
        return new ItemViewHolder(viewProvider, parent);
    }

    public int getViewType() {
        return viewType;
    }

    @Override
    public View createView(ViewGroup parent) {
        return viewProvider.createView(parent);
    }

    @Override
    public void bindData(int position) {
        viewProvider.bindData(position);
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private ItemViewProvider provider;

        ItemViewHolder(ItemViewProvider provider, ViewGroup parent) {
            super(provider.createView(parent));
            this.provider = provider;
        }

        public void bindData(int position) {
            provider.bindData(position);
        }
    }
}
