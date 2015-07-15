package org.sopt.linkbox.debugging;

import android.util.Log;

import retrofit.RetrofitError;

/**
 * Created by Junyoung on 2015-07-15.
 */
public class RetrofitDebug {
    private static final String TAG = "TEST/Retrofit : ";
    public static void debug(RetrofitError error) {
        Log.d(TAG, "URL : " + error.getUrl());
        Log.d(TAG, "BODY : " + error.getBody().toString());
        Log.d(TAG, "MESSAGE : " + error.getMessage());
    }
}
