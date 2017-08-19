package com.youga.recyclerwrapper;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;

import com.youga.recyclerwrapper.adapter.InteractionListener;
import com.youga.recyclerwrapper.adapter.RealAdapter;
import com.youga.recyclerwrapper.core.FillWrapper;
import com.youga.recyclerwrapper.core.FootWrapper;

/**
 * Created by Youga on 2017/8/17.
 */

public class Wrapper {

    public int width;
    public int height;
    public RecyclerView.LayoutManager layoutManager;
    public FillWrapper fillWrapper;
    public FootWrapper footWrapper;
    public DisplayMetrics metrics;
    public LoadMoreListener loadMoreListener;
    public InteractionListener interactionListener;
    public RecyclerView.Adapter adapter;

}
