package org.sopt.linkbox.custom.network;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-07-14.
 */
public class MainServerWrapper {
    private static MainServerInterface mainServerInterface = null;


    private static void fillInterface() {
        if (mainServerInterface == null) {
            mainServerInterface = LinkBoxController.getApplication().getLinkNetworkMainServerInterface();
        }
    }


    public MainServerWrapper() {
        fillInterface();
    }


    public void postLoginAsync(String email, String pass, Callback<MainServerData<UserData>> callback) {
        UserData userData = new UserData();
        userData.usrID = email;
        userData.usrPassword = pass;
        mainServerInterface.postLoginAsync(userData, callback);
    }
    public void postSignUpAsync(String email, String name, String pass, Callback<MainServerData<UserData>> callback) {
        UserData userData = new UserData();
        userData.usrID = email;
        userData.usrName = name;
        userData.usrPassword = pass;
        mainServerInterface.postSignUpAsync(userData, callback);
    }
    public void postFacebookAccessAsync(String email, String name, String profile, String pass, Callback<MainServerData<UserData>> callback) {
        UserData userData = new UserData();
        userData.usrID = email;
        userData.usrName = name;
        userData.usrProfile = profile;
        userData.usrPassword = pass;
        mainServerInterface.postFacebookAccessAsync(userData, callback);
    }


    public void getBoxListAsync(Callback<MainServerData<List<BoxListData>>> callback) {
        mainServerInterface.getBoxListAsync(LinkBoxController.userData.usrKey, callback);
    }
    public void postAddBoxAsync(String cbname, Callback<MainServerData<BoxListData>> callback) {
        BoxListData boxListData = new BoxListData();
        boxListData.boxName = cbname;
        boxListData.boxIndex = LinkBoxController.boxListSource.size();
        mainServerInterface.postAddBoxAsync(LinkBoxController.userData.usrKey, boxListData, callback);
    }
    public void postRemoveBoxAsync(int cbindex, Callback<MainServerData<Object>> callback) {
        BoxListData boxListData = LinkBoxController.boxListSource.get(cbindex);
        mainServerInterface.postRemoveBoxAsync(LinkBoxController.userData.usrKey, boxListData, callback);
    }
    public void postEditBoxAsync(int cbindex, String newname, Callback<MainServerData<BoxListData>> callback) {
        BoxListData boxListData = LinkBoxController.boxListSource.get(cbindex);
        boxListData.boxName = newname;
        mainServerInterface.postEditBoxAsync(LinkBoxController.userData.usrKey, boxListData, callback);
    }
    public void postMoveBoxAsync(int cbindex1, int cbindex2, Callback<MainServerData<Object>> callback){
        ArrayList<BoxListData> boxListDatas = new ArrayList<>(LinkBoxController.boxListSource);
        changeBoxSort(cbindex1, cbindex2, boxListDatas);
    }


    private void changeBoxSort(int cbindex1, int cbindex2, ArrayList<BoxListData> list) {
        BoxListData boxListData = list.get(cbindex1);
        list.remove(cbindex1);
        list.add(cbindex2, boxListData);
    }


    public void getBoxUrlListAsync(Callback<MainServerData<List<UrlListData>>> callback) {
        mainServerInterface.getBoxUrlListAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, callback);
    }
    public void postAddUrlAsync(String url, String name, String thumb, Callback<MainServerData<UrlListData>> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.url = url;
        urlListData.urlTitle = name;
        urlListData.urlThumbnail = thumb;
        mainServerInterface.postAddUrlAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void postRemoveUrlAsync(int urlid, Callback<MainServerData<Object>> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.urlKey = urlid;
        mainServerInterface.postRemoveUrlAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void postEditUrlAsync(int urlid, String newname, Callback<MainServerData<UrlListData>> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.urlKey = urlid;
        urlListData.urlTitle = newname;
        mainServerInterface.postEditUrlAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, urlListData, callback);
    }
    public void postAddGoodAsync(int urlid, Callback<MainServerData<Object>> callback) {
        mainServerInterface.postAddGoodAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, urlid, callback);
    }
    public void postRemoveGoodAsync(int urlid, Callback<MainServerData<Object>> callback) {
        mainServerInterface.postRemoveGoodAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, urlid, callback);
    }


    public void getUsrListAsync(Callback<MainServerData<List<UserData>>> callback) {
        mainServerInterface.getUsrListAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, callback);
    }
    public void postAddUsrAsync(String usrID, String message, Callback<MainServerData<Object>> callback) {
        TwoString twoString = new TwoString();
        twoString.usrID = usrID;
        twoString.message = message;
        mainServerInterface.postAddUsrAsync(LinkBoxController.userData.usrKey, LinkBoxController.currentBox.boxKey, twoString, callback);
    }


    public void postSignDownAsync(String pass, Callback<MainServerData<Object>> callback) {
        UserData userData = new UserData();
        userData.usrKey = LinkBoxController.userData.usrKey;
        userData.usrID = LinkBoxController.userData.usrID;
        userData.usrPassword = pass;
        mainServerInterface.postSignDownAsync(userData, callback);
    }


    public void postRegisterTokenAsync(String token, Callback<MainServerData<Object>> callback) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mainServerInterface.postRegisterTokenAsync(LinkBoxController.userData.usrKey, jsonObject, callback);
    }


    public void postPremium(Callback<MainServerData<Object>> callback) {
        mainServerInterface.postPremium(LinkBoxController.userData, callback);
    }
}