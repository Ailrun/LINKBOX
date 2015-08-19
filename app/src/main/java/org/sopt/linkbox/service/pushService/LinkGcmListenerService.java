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
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.activity.mainPage.boxListPage.InvitedBoxActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.R;
import org.sopt.linkbox.constant.GCMString;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class LinkGcmListenerService extends GcmListenerService{
    private static final String TAG = LinkGcmListenerService.class.getName();
    private static final String NotiTitle = "Linkbox";
    private static final int boxNotiOffset = 0;
    private static final int urlNotiOffset = 0x55;
    private static final int goodNotiOffset = 0x99;

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private NotificationManager nm = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    public void onMessageReceived(String from, Bundle data) {
        nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String json = data.getString("object");
        JSONObject jsonObject = null;
        Log.d(TAG, json);
        try {
            jsonObject = new JSONObject(json);
            Log.d(TAG, jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonObject != null) {
            String type = jsonObject.optString(GCMString.pushType);
            if (type != null) {
                switch (type) {
                    case GCMString.typeBox:
                        boxNotification(jsonObject);
                        break;
                    case GCMString.typeUrl:
                        urlNotification(jsonObject);
                        break;
                    case GCMString.typeGood:
                        goodNotification(jsonObject);
                        break;
                }
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Set Notifications" defaultstate="collapsed">
    private void boxNotification(JSONObject jsonObject) {
        AlarmListData alarmListData = new AlarmListData();
        alarmListData.alarmKey = jsonObject.optInt(GCMString.alarmKey);
        alarmListData.alarmSetUsrName = jsonObject.optString(GCMString.usrName);
        alarmListData.alarmUrlKey = jsonObject.optInt(GCMString.urlKey);
        alarmListData.alarmUrlTitle = jsonObject.optString(GCMString.urlTitle);
        alarmListData.alarmBoxKey = jsonObject.optInt(GCMString.boxKey);
        alarmListData.alarmBoxName = jsonObject.optString(GCMString.boxName);
        alarmListData.alarmMessage = jsonObject.optString(GCMString.message);
        alarmListData.alarmDate = jsonObject.optString(GCMString.date);
        if (LinkBoxController.invitedBoxListSource != null) {
            LinkBoxController.invitedBoxListSource.add(alarmListData);
            LinkBoxController.notifyInvitedDataSetChanged();
        }
        Intent intent = new Intent(this, InvitedBoxActivity.class);
        intent.putExtra(GCMString.isPush, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker("새로운 박스가 공유되었습니다.");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1); //TODO : Set Number = What does the number of each noti mean? #noti of each box? #noti of each user?
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentTitle(NotiTitle);
        builder.setContentText(alarmListData.alarmSetUsrName + " 님이 \""
                + alarmListData.alarmBoxName + "\" 박스로 초대하셨습니다.");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        nm.notify(boxNotiOffset, builder.build());
    }
    private void urlNotification(JSONObject jsonObject) {
        Intent intent = new Intent(this, LinkBoxActivity.class);
        intent.putExtra(GCMString.isPush, true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker("새로운 링크가 올라왔습니다.");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentTitle(NotiTitle);
        builder.setContentText(jsonObject.optString(GCMString.usrName) + " 님이 \""
                + jsonObject.optString(GCMString.boxName) + "\" 박스에 " + jsonObject.optString(GCMString.url) + "를 추가하셨습니다.");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        nm.notify(jsonObject.optInt(GCMString.boxKey) + urlNotiOffset, builder.build());
    }
    private void goodNotification(JSONObject jsonObject) {
        Intent intent = new Intent(this, LinkBoxActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.logo);
        builder.setTicker("링크에 좋아요를 받으셨습니다.");
        builder.setWhen(System.currentTimeMillis());
        builder.setNumber(1);
        builder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        builder.setContentTitle(NotiTitle);
        builder.setContentText(jsonObject.optString(GCMString.usrName) + " 님이 회원님의 링크에 좋아요를 눌렀습니다.");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        nm.notify(goodNotiOffset, builder.build());
    }
    //</editor-fold>

    //<editor-fold desc="Set Badges" defaultstate="collapsed">
    private void setIconBadge(int i) {  // TODO : Amount of push alarm. Ex) facebook alarm
        ShortcutBadger.with(getApplicationContext()).count(1);
    }
    //</editor-fold>
}
