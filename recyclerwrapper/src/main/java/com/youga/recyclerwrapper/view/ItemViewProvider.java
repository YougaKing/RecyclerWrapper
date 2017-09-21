package com.youga.recyclerwrapper.view;

import android.view.View;
import android.view.ViewGroup;


/**
 * Created by Youga on 2017/8/18.
 */

public interface ItemViewProvider {

    View createView(ViewGroup parent);

    void bindData(int position);
}
