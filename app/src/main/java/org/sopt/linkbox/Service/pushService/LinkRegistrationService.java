package org.sopt.linkbox.service.pushService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.sopt.linkbox.R;

import java.io.IOException;

/**
 * Created by Junyoung on 2015-07-06.
 */
public class LinkRegistrationService extends IntentService {
    private static final String TAG = LinkRegistrationService.class.getName();

    public LinkRegistrationService() {
        super(LinkRegistrationService.class.getName());
    }

    public LinkRegistrationService(String name) {
        super(name);
    }

    private String token;
    public String getToken() {return token;}

    @Override
    protected void onHandleIntent(Intent intent) {
        synchronized (TAG) {
            try {
                InstanceID instanceID = InstanceID.getInstance(this);
                token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);

                Log.d("TEST", "GCM Token : " + token);
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }
}
