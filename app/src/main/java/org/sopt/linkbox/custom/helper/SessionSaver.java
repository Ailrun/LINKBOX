package org.sopt.linkbox.custom.helper;

import android.app.Activity;

/**
 * Created by Junyoung on 2015-07-23.
 */
public class SessionSaver {
    private static Activity activity = null;
    public static void saveSession(Activity activity) {
        SessionSaver.activity = activity;
    }
    public static Activity recallSession() {
        return SessionSaver.activity;
    }
}