package org.sopt.linkbox.service.pushService;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by Junyoung on 2015-07-06.
 */
public class LinkInstanceIDListenerService extends InstanceIDListenerService {
    private static final String TAG = LinkInstanceIDListenerService.class.getName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        Intent intent = new Intent(this, LinkRegistrationService.class);
        startService(intent);
    }
}
