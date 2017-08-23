package com.youga.sample;

/**
 * Created by YougaKing on 2016/10/9.
 */

public interface HttpCallback<T> {

    void onFailure(String message);

    void onResponse(T t, String message);
}
