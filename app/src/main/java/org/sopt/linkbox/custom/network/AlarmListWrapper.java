package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;

import java.util.List;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-08-08.
 */
public class AlarmListWrapper {
    private static AlarmListInterface alarmListInterface = null;

    private static void setAlarmListInterface() {
        if (alarmListInterface == null) {
            alarmListInterface = LinkBoxController.getApplication().getAlarmListInterface();
        }
    }

    public AlarmListWrapper() {
        setAlarmListInterface();
    }

    public void allList(Callback<MainServerData<List<AlarmListData>>> callback) {
        alarmListInterface.allList(LinkBoxController.usrListData.usrKey, callback);
    }
    public void hiddenList(Callback<MainServerData<List<AlarmListData>>> callback) {
        alarmListInterface.hiddenList(LinkBoxController.usrListData.usrKey, callback);
    }
    public void hidden(AlarmListData alarmListData, Callback<MainServerData<Object>> callback) {
        alarmListInterface.hidden(LinkBoxController.usrListData.usrKey, alarmListData.alarmKey, callback);
    }
}
