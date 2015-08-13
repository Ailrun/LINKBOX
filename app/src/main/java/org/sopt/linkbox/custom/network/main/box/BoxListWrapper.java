package org.sopt.linkbox.custom.network.main.box;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;

import java.util.List;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-08-08.
 */
public class BoxListWrapper {
    private static BoxListInterface boxListInterface = null;

    private static void setBoxListInterface() {
        if (boxListInterface == null) {
            boxListInterface = LinkBoxController.getApplication().getBoxListInterface();
        }
    }

    public BoxListWrapper() {
        setBoxListInterface();
    }

    public void list(Callback<MainServerData<List<BoxListData>>> callback) {
        boxListInterface.list(LinkBoxController.usrListData.usrKey, callback);
    }
    public void add(BoxListData boxListData, Callback<MainServerData<BoxListData>> callback) {
        boxListInterface.add(LinkBoxController.usrListData.usrKey, boxListData, callback);
    }
    public void remove(BoxListData boxListData, Callback<MainServerData<Object>> callback) {
        boxListInterface.remove(LinkBoxController.usrListData.usrKey, boxListData, callback);
    }
    public void edit(BoxListData boxListData, Callback<MainServerData<Object>> callback) {
        boxListInterface.edit(LinkBoxController.usrListData.usrKey, boxListData, callback);
    }
    public void favorite(BoxListData boxListData, Callback<MainServerData<Object>> callback) {
        boxListInterface.favorite(LinkBoxController.usrListData.usrKey, boxListData, callback);
    }
    public void invite(TwoString twoString, Callback<MainServerData<Object>> callback) {
        boxListInterface.invite(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox.boxKey, twoString, callback);
    }
    public void accept(AlarmListData alarmListData, int index, Callback<MainServerData<Object>> callback) {
        BoxListData boxListData = new BoxListData();
        boxListData.boxIndex = index;
        boxListInterface.accept(alarmListData.alarmKey, boxListData, callback);
    }
    public void decline(AlarmListData alarmListData, Callback<MainServerData<Object>> callback) {
        boxListInterface.decline(alarmListData.alarmKey, callback);
    }
    public void editorList(Callback<MainServerData<List<UsrListData>>> callback) {
        boxListInterface.editorList(LinkBoxController.usrListData.usrKey, LinkBoxController.currentBox, callback);
    }
}
