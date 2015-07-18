package org.sopt.linkbox.debugging;

import android.util.Log;

import com.facebook.FacebookException;

/**
 * Created by Junyoung on 2015-07-16.
 */
public class FacebookDebug {
    private static final String TAG = "TEST/Facebook : ";
    public static void debug(FacebookException exception) {
        if (exception != null) {
            Log.d(TAG, exception.getMessage());
        }
        else {
            Log.d(TAG, "Null Exception");
        }
    }
}
