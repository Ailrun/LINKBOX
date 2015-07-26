package org.sopt.linkbox.service.pushService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.activity.mainPage.LinkBoxActivity;
import org.sopt.linkbox.R;
import org.sopt.linkbox.constant.GCMString;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class LinkGcmListenerService extends GcmListenerService{
    private static final String TAG = LinkGcmListenerService.class.getName();

    @Override
    public void onMessageReceived(String from, Bundle data) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, LinkBoxActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_NO_HISTORY);
        String jsonString = data.toString();
        JSONObject jsonObject = null;
        Log.d(TAG, jsonString);
        try {
            jsonObject = new JSONObject(jsonString);
            Log.d(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            String type = jsonObject.optString(GCMString.pushType);
            if (type != null) {
                if (type.equals(GCMString.typeBox)) {
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.mipmap.logo);
                    builder.setTicker("새로운 링크가 올라왔습니다.");
                    builder.setWhen(System.currentTimeMillis());
                    builder.setNumber(1);
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                    builder.setContentTitle("LINK BOX");
                    builder.setContentText(data.getString("boxName") + "박스의 " + data.getString("username") + " 님이 "+ data.getString("url") + "를 추가하셨습니다.");
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setPriority(Notification.PRIORITY_MAX);
                    nm.notify(data.getInt("boxKey") + 1, builder.build());
                }
                else if (type.equals(GCMString.typeUrl)) {
                    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                    Notification.Builder builder = new Notification.Builder(this);
                    builder.setSmallIcon(R.mipmap.logo);
                    builder.setTicker("새로운 링크가 올라왔습니다.");
                    builder.setWhen(System.currentTimeMillis());
                    builder.setNumber(1);
                    builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                    builder.setContentTitle("LINK BOX");
                    builder.setContentText(data.getString("boxName") + "박스의 " + data.getString("username") + " 님이 "+ data.getString("url") + "를 추가하셨습니다.");
                    builder.setContentIntent(pendingIntent);
                    builder.setAutoCancel(true);
                    builder.setPriority(Notification.PRIORITY_MAX);
                    nm.notify(data.getInt("boxKey") + 1, builder.build());
                }
                else if (type.equals(GCMString.typeGood)) {

                }
            }
        }
    }

    private void boxNotification(JSONObject jsonObject) {

    }
    private void urlNotification(JSONObject jsonObject) {

    }
    private void goodNotification(JSONObject jsonObject) {

    }
}
