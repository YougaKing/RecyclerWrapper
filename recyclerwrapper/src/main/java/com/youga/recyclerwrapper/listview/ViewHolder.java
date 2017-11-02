package com.youga.recyclerwrapper.listview;

import android.view.View;

/**
 * @author YougaKingWu
 * @descibe ...
 * @date 2017/11/2 0002-14:28
 */

public class ViewHolder {

    protected final View itemView;

    public ViewHolder(View itemView) {
        if (itemView == null) {
            throw new IllegalArgumentException("itemView may not be null");
        }
        this.itemView = itemView;
    }

    protected View getView() {
        return itemView;
    }
}
