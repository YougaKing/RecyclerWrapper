package com.youga.sample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WuXiaolong on 2015/7/2.
 */
public class RecyclerViewAdapter extends BaseAdapter<User> {

    public RecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindPosition(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, mTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mTextView = (TextView) itemView.findViewById(R.id.tv_position);
        }

        public void bindPosition(int position) {
            mTextView.setText("position:" + position + "--\tsize:" + mList.size());
//            User user = mList.get(position);
//            title.setText(user.getLogin());
        }
    }


}
