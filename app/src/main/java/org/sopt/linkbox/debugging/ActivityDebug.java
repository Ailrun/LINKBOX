package org.sopt.linkbox.debugging;

import android.app.Activity;
import android.util.Log;

/**
 * Created by Junyoung on 2015-07-29.
 */
public class ActivityDebug {
    private static final String TAG = "TEST/" + ActivityDebug.class.getName() + " : ";
    public static void debug(Activity activity, String string) {
        Log.d(TAG, "Activity" + activity.getLocalClassName() + " is now = " + string);
    }
}
