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

public class MainActivity extends AppCompatActivity {

    static final String REFRESH = "REFRESH", LOAD_MORE = "LOAD_MORE";
    private RecyclerView mRecyclerView;
    private BaseAdapter<User> mAdapter;
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mNotMore;
    private InteractionListener.RevealListener mRevealListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        init();
    }

    private void init() {

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.refresh:
                        mNotMore = false;
                        requestList(REFRESH, 0);
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

        mRevealListener = RecyclerWrapper.with(mRecyclerView)
                .wrapper(new LoadMoreListener() {
                    @Override
                    public void onLoadMore(int position) {
                        onDrag();
                    }
                });

        mRevealListener.showLoadView(null);
        requestList(REFRESH, 0);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNotMore = false;
                requestList(REFRESH, 0);
            }
        });
    }

    private void onDrag() {
        User user = mAdapter.getLastNumber();
        requestList(LOAD_MORE, user.getId());
    }

    private class HeaderViewProvider implements ItemViewProvider {

        private TextView mTextView;

        @Override
        public View createView(ViewGroup parent) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header_view, parent, false);
            mTextView = (TextView) v.findViewById(R.id.textView);
            return v;
        }

        @Override
        public void bindData(int position) {
            mTextView.setText(String.valueOf(position));
        }
    }

    void requestList(final String type, int since) {
        Type typeToken = new TypeToken<List<User>>() {
        }.getType();
        HttpUtil.getAllUsers(LOAD_MORE.equals(type) ? since : 0, typeToken, new HttpCallback<List<User>>() {
            @Override
            public void onFailure(String message) {
                switch (type) {
                    case REFRESH:
                        mSwipeRefreshLayout.setEnabled(true);
                        mSwipeRefreshLayout.setRefreshing(false);
                        showToast(message);
                        mRevealListener.showErrorView(null);
                        break;
                    case LOAD_MORE:
                        showToast(message);
                        mRevealListener.showErrorView(null);
                        break;
                }
            }

            @Override
            public void onResponse(List<User> users, String message) {
                switch (type) {
                    case REFRESH:
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
                        break;
                    case LOAD_MORE:
                        if (mNotMore) users.remove(0);
                        mAdapter.getDataList().addAll(users);
                        mAdapter.notifyDataSetChanged();
                        mRevealListener.decideMore(users.size() >= 5);
                        break;
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
