package org.sopt.linkbox.debugging;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class TaskDebug {
    private static final String TAG = "TEST/Task : ";
    public static void debug(Activity activity) {
        ActivityManager am = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> task = am.getRunningTasks(1); //(숫자)는 가져올 Task의 최대갯수

        ComponentName baseActivity = task.get(0).baseActivity;
        String baseClass = baseActivity.getClassName(); //base activity 이름.
        ComponentName topActivity = task.get(0).topActivity;
        String topClass = topActivity.getClassName(); //top activity 이름
        int numActivities = task.get(0).numActivities;
        Log.d(TAG, "Base class = " + baseClass);
        Log.d(TAG, "Top class = " + topClass);
        Log.d(TAG, "Num Activities = " + numActivities);
    }
}
