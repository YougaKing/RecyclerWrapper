package com.youga.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.gson.reflect.TypeToken;
import com.youga.recyclerwrapper.LoadMoreListener;
import com.youga.recyclerwrapper.RecyclerWrapper;
import com.youga.recyclerwrapper.core.InteractionListener;
import com.youga.recyclerwrapper.view.ItemViewProvider;

import java.lang.reflect.Type;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private BaseAdapter<User> mAdapter;
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mNotMore;
    private InteractionListener.RevealListener mRevealListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        init();
    }

    private void init() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
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
                        mRecyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
                        break;
                }
                return false;
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mAdapter = new RecyclerViewAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        mRevealListener = RecyclerWrapper.with(mRecyclerView).wrapper(new LoadMoreListener() {
            @Override
            public void onLoadMore(int position) {
                User user = mAdapter.getLastNumber();
                requestList(user.getId());
            }
        });

        mRevealListener.showLoadView(null);
        requestList(0);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNotMore = false;
                requestList(0);
            }
        });
    }

    void requestList(final int since) {
        HttpUtil.getAllUsers(since, new HttpCallback<List<User>>() {
            @Override
            public void onFailure(String message) {
                if (since == 0) {
                    mSwipeRefreshLayout.setEnabled(true);
                    mSwipeRefreshLayout.setRefreshing(false);
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
                    mSwipeRefreshLayout.setEnabled(true);
                    mSwipeRefreshLayout.setRefreshing(false);
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    void showToast(String message) {
        Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT).show();
    }
}
