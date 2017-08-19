package com.youga.sample;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YougaKing on 2016/10/9.
 */

public class HttpUtil {

    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Gson mGson = new Gson();
    private volatile static HttpUtil mInstance;

    private HttpUtil() {
    }

    private static HttpUtil getInstance() {
        if (mInstance == null) {
            synchronized (HttpUtil.class) {
                if (mInstance == null)
                    mInstance = new HttpUtil();
            }

        }
        return mInstance;
    }


    public static <T> void getAllUsers(int since, final Type type, final HttpCallback<T> callback) {
        final String url = "https://api.github.com/users?per_page=5";
        Request request = new Request
                .Builder()
                .url(since == 0 ? url : url + "&since=" + since)
                .build();
        getInstance().mOkHttpClient
                .newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        postMain(new Runnable() {
                            @Override
                            public void run() {
                                callback.onFailure(e.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onResponse(final Call call, final Response response) throws IOException {
                        try {
                            String json = response.body().string();
                            final T users = getInstance().mGson.fromJson(json, type);
                            postMain(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onResponse(users, "success");
                                }
                            });
                        } catch (final IOException e) {
                            e.printStackTrace();
                            postMain(new Runnable() {
                                @Override
                                public void run() {
                                    callback.onFailure(e.getMessage());
                                }
                            });
                        }

                    }
                });

    }

    private static void postMain(Runnable runnable) {
        getInstance().mHandler.post(runnable);
    }

}
