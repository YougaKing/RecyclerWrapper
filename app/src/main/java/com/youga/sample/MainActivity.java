package com.youga.sample;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.google.gson.reflect.TypeToken;
import com.youga.recyclerwrapper.LoadMoreListener;
import com.youga.recyclerwrapper.RecyclerWrapper;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;
import com.youga.recyclerwrapper.view.IFillViewProvider;
import com.youga.recyclerwrapper.view.IFootViewProvider;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String REFRESH = "REFRESH", LOAD_MORE = "LOAD_MORE";
    private RecyclerView mRecyclerView;
    private BaseAdapter<User> mAdapter;
    private Toolbar toolbar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mNotMore;

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
                        RecyclerWrapper.showLoadView("手动刷新加载中....");//加载显示ProgressBar
                        break;
                    case R.id.empty:
                        mAdapter.getDataList().clear();
                        RecyclerWrapper.showEmptyView("手动显示空布局");//显示请求结果为空时显示
                        break;
                    case R.id.error:
                        mAdapter.getDataList().clear();
                        RecyclerWrapper.showErrorView("手动显示加载错误");//显示请求错误时显示
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

        RecyclerWrapper.with(mRecyclerView)
                .fillView(new IFillViewProvider<View, String>() {
                    @Override
                    public View createView() {
                        return LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.fill_layout, mRecyclerView, false);
                    }

                    @Override
                    public void bindView(View view, String string, @FillWrapper.Type int type) {
                        TextView textView = (TextView) view.findViewById(R.id.text);
                        switch (type) {
                            case FillWrapper.LOAD:
                                textView.setText("FillWrapper.LOAD:" + string);
                                break;
                            case FillWrapper.EMPTY:
                                textView.setText("FillWrapper.EMPTY:" + string);
                                break;
                            case FillWrapper.ERROR:
                                textView.setText("FillWrapper.ERROR:" + string);
                                break;
                        }
                    }
                })
                .footView(new IFootViewProvider<View, String>() {
                    @Override
                    public View createView() {
                        return LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.nav_header_main, mRecyclerView, false);
                    }

                    @Override
                    public void bindView(View view, String string, @FootWrapper.Type int type) {
                        TextView textView = (TextView) view.findViewById(R.id.textView);
                        switch (type) {
                            case FootWrapper.F_LOAD:
                                textView.setText("FootWrapper.F_LOAD:" + string);
                                break;
                            case FootWrapper.F_FAULT:
                                textView.setText("FootWrapper.F_FAULT:" + string);
                                break;
                        }
                    }
                })
                .wrapper(new LoadMoreListener() {
                    @Override
                    public void onLoadMore(int position) {
                        onDrag();
                    }
                });

        RecyclerWrapper.showLoadView("第一次加载中....");
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
                        RecyclerWrapper.showErrorView(message);
                        break;
                    case LOAD_MORE:
                        showToast(message);
                        RecyclerWrapper.loadMoreFault(message);
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
                        RecyclerWrapper.haveMore(users.size() >= 5, "已记载" + mAdapter.getItemCount() + "条");
                        if (users.size() == 0) {
                            RecyclerWrapper.showEmptyView("神马都没有");//显示请求结果为空时显示
                        }
                        break;
                    case LOAD_MORE:
                        if (mNotMore) users.remove(0);
                        mAdapter.getDataList().addAll(users);
                        mAdapter.notifyDataSetChanged();
                        RecyclerWrapper.haveMore(users.size() >= 5, "已记载" + mAdapter.getItemCount() + "条");
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
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.lm_hor) {
//            mAdapter = new RecyclerViewAdapter(this);
//            mDragRecyclerView.setAdapter(mAdapter, true, new LinearLayoutManager(this,
//                    LinearLayoutManager.HORIZONTAL, false));
//        } else if (id == R.id.lm_ver) {
//            mAdapter = new RecyclerViewAdapter(this);
//            mDragRecyclerView.setAdapter(mAdapter, true, new LinearLayoutManager(this,
//                    LinearLayoutManager.VERTICAL, false));
//        } else if (id == R.id.glm_hor) {
//            mAdapter = new StaggeredGridAdapter(this, false);
//            mDragRecyclerView.setAdapter(mAdapter, true, new GridLayoutManager(this, 4,
//                    GridLayoutManager.HORIZONTAL, false));
//        } else if (id == R.id.glm_ver) {
//            mAdapter = new StaggeredGridAdapter(this, false);
//            mDragRecyclerView.setAdapter(mAdapter, true, new GridLayoutManager(this, 4,
//                    GridLayoutManager.VERTICAL, false));
//        } else if (id == R.id.sglm_hor) {
//            mAdapter = new StaggeredGridAdapter(this, true);
//            mDragRecyclerView.setAdapter(mAdapter, true,
//                    new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.HORIZONTAL));
//        } else if (id == R.id.sglm_ver) {
//            mAdapter = new StaggeredGridAdapter(this, true);
//            mDragRecyclerView.setAdapter(mAdapter, true,
//                    new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
//        }
//        mDragRecyclerView.showLoadingView();
//        mNotMore = false;
//        requestList(REFRESH, 0);
//
//        mDragRecyclerView.setOnDragListener(new DragRecyclerView.OnDragListener() {
//            @Override
//            public void onLoadMore() {
//                onDrag();
//            }
//        });
//
//
//        mDragRecyclerView.setOnItemClickListener(new DragRecyclerView.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "position:" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

    void showToast(String message) {
        Snackbar.make(toolbar, message, Snackbar.LENGTH_SHORT).show();
    }
}
