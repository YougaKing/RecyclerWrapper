package com.youga.sample;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by YougaKing on 2016/8/11.
 */
public class StaggeredGridAdapter extends BaseAdapter<User> {

    private final boolean mRandom;

    public StaggeredGridAdapter(Context context, boolean random) {
        super(context);
        mRandom = random;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.bindView(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, mTextView;
        private CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            mTextView = (TextView) itemView.findViewById(R.id.tv_position);
            mCardView = (CardView) itemView.findViewById(R.id.card_view);
        }

        public void bindView(int position) {
            if (mRandom) {
                ViewGroup.LayoutParams lp = mCardView.getLayoutParams();
                lp.height = (int) (200 + Math.random() * 300);
                mCardView.setLayoutParams(lp);
            }
            mTextView.setText(String.valueOf(position + 1));
            User user = mList.get(position);
            title.setText(user.getLogin());

        }
    }
}
