package com.youga.recyclerwrapper.listview;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/11/2 0002-13:59
 */

class AdapterWrapper extends BaseAdapter {

    private BaseAdapter mAdapter;

    AdapterWrapper(BaseAdapter adapter) {
        mAdapter = adapter;
        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                AdapterWrapper.this.notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                AdapterWrapper.this.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public Object getItem(int position) {
        return mAdapter.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mAdapter.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mAdapter.getView(position, convertView, parent);
    }

    public BaseAdapter getAdapter() {
        return mAdapter;
    }
}
