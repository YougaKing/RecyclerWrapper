package com.youga.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YougaKing on 2016/8/11.
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected String TAG = getClass().getSimpleName();
    protected Context mContext;
    protected List<T> mList = new ArrayList<>();

    protected List<T> getDataList() {
        return mList;
    }

    public BaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public T getFirstNumber() {
        return mList.get(0);
    }

    public T getLastNumber() {
        return mList.get(getItemCount() - 1);
    }
}
