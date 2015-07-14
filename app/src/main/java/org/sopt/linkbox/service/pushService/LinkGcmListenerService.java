package org.sopt.linkbox.service.pushService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;

import org.sopt.linkbox.LinkBoxActivity;
import org.sopt.linkbox.R;

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
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker("새로운 링크가 올라왔습니다.");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentTitle("LINK BOX");
        builder.setContentText(data.getString("cbname") + "박스의 " + data.getString("username") + " 님이 "+ data.getString("address") + "를 추가하셨습니다.");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        nm.notify(data.getInt("cbid") + 1, builder.build());
    }
}
