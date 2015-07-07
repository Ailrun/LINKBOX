package org.sopt.linkbox.service.pushService;

import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Created by Junyoung on 2015-07-06.
 */
public class LinkGcmListenerService extends GcmListenerService{
    private static final String TAG = LinkGcmListenerService.class.getName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        /*
        TODO :Make Noti Message for Push alarm
         */
    }
}
