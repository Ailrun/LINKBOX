package org.sopt.linkbox.service.pushService;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.MainServerWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.IOException;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
                new MainServerWrapper().postRegisterTokenAsync(token, new RegisterTokenCallback());
                Log.d("TEST", "GCM Token : " + token);
            }
            catch (IOException e) { e.printStackTrace(); }
        }
    }

    private class RegisterTokenCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {

        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }
}
