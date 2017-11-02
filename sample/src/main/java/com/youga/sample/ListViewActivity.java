package com.youga.sample;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.youga.recyclerwrapper.LoadMoreListener;
import com.youga.recyclerwrapper.RecyclerWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity {

    private ListView mListView;
    private InnerAdapter mAdapter;
    private InteractionListener.RevealListener mRevealListener;
    private boolean mNotMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        mListView = (ListView) findViewById(R.id.listView);

        mAdapter = new InnerAdapter();
        mListView.setAdapter(mAdapter);

        mRevealListener = RecyclerWrapper.with(mListView).wrapper(new LoadMoreListener() {
            @Override
            public void onLoadMore(int position) {
                User user = mAdapter.getLastNumber();
                requestList(user.getId());
            }
        });

        mRevealListener.showLoadView(null);
        requestList(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    void requestList(final int since) {
        HttpUtil.getAllUsers(since, new HttpCallback<List<User>>() {
            @Override
            public void onFailure(String message) {
                if (since == 0) {
                    showToast(message);
                    mRevealListener.showErrorView(null);
                } else {
                    showToast(message);
                    mRevealListener.showErrorView(null);
                }
            }

            @Override
            public void onResponse(List<User> users, String message) {
                if (since == 0) {
                    mAdapter.getDataList().clear();
                    mAdapter.getDataList().addAll(users);
                    mAdapter.notifyDataSetChanged();
                    if (users.size() == 0) {
                        mRevealListener.showEmptyView(null);//显示请求结果为空时显示
                    } else {
                        mRevealListener.decideMore(users.size() >= 5);
                    }
                } else {
                    if (mNotMore) users.remove(0);
                    mAdapter.getDataList().addAll(users);
                    mAdapter.notifyDataSetChanged();
                    mRevealListener.decideMore(users.size() >= 5);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                mNotMore = false;
                requestList(0);
                mRevealListener.showLoadView(null);
                break;
            case R.id.empty:
                mAdapter.getDataList().clear();
                mRevealListener.showEmptyView(null);
                break;
            case R.id.error:
                mAdapter.getDataList().clear();
                mRevealListener.showErrorView(null);
                break;
            case R.id.not_more:
                mNotMore = true;
                mListView.setSelection(mAdapter.getCount() - 1);
                break;
        }
        return false;
    }


    void showToast(String message) {
        Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_SHORT).show();
    }

    public class InnerAdapter extends android.widget.BaseAdapter {


        List<User> mList = new ArrayList<>();

        List<User> getDataList() {
            return mList;
        }

        public User getLastNumber() {
            return mList.get(getCount() - 1);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.bindPosition(position);
            return convertView;
        }

        public class ViewHolder {
            public TextView title, mTextView;

            public ViewHolder(View itemView) {
                title = (TextView) itemView.findViewById(R.id.title);
                mTextView = (TextView) itemView.findViewById(R.id.tv_position);
            }

            public void bindPosition(int position) {
                mTextView.setText("position:" + position + "--\tsize:" + mList.size());
            }
        }
    }
}
