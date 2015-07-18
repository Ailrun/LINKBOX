package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.GoodData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
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


    public void postLoginAsync(String email, String pass, Callback<UserData> callback) {
        UserData userData = new UserData();
        userData.usremail = email;
        userData.pass = pass;
        mainServerInterface.postLoginAsync(userData, callback);
    }
    public void postSignUpAsync(String email, String name, String pass, Callback<UserData> callback) {
        UserData userData = new UserData();
        userData.usremail = email;
        userData.usrname = name;
        userData.pass = pass;
        mainServerInterface.postSignUpAsync(userData, callback);
    }
    public void postFacebookAccessAsync(String email, String name, String profile, String pass, Callback<UserData> callback) {
        UserData userData = new UserData();
        userData.usremail = email;
        userData.usrname = name;
        userData.usrprofile = profile;
        userData.pass = pass;
        mainServerInterface.postFacebookAccessAsync(userData, callback);
    }


    public void getBoxListAsync(Callback<List<BoxListData>> callback) {
        mainServerInterface.getBoxListAsync(LinkBoxController.userData.usrid, callback);
    }
    public void postAddBoxAsync(String cbname, Callback<BoxListData> callback) {
        BoxListData boxListData = new BoxListData();
        boxListData.cbname = cbname;
        boxListData.index = LinkBoxController.boxListSource.size();
        mainServerInterface.postAddBoxAsync(LinkBoxController.userData.usrid, boxListData, callback);
    }
    public void postRemoveBoxAsync(int cbindex, Callback<Object> callback) {
        BoxListData boxListData = LinkBoxController.boxListSource.get(cbindex);
        mainServerInterface.postRemoveBoxAsync(LinkBoxController.userData.usrid, boxListData, callback);
    }
    public void postEditBoxAsync(int cbindex, String newname, Callback<BoxListData> callback) {
        BoxListData boxListData = LinkBoxController.boxListSource.get(cbindex);
        boxListData.cbname = newname;
        mainServerInterface.postEditBoxAsync(LinkBoxController.userData.usrid, boxListData, callback);
    }
    public void postMoveBoxAsync(int cbindex1, int cbindex2, Callback<Object> callback){
        ArrayList<BoxListData> boxListDatas = new ArrayList<>(LinkBoxController.boxListSource);
        changeBoxSort(cbindex1, cbindex2, boxListDatas);
    }


    private void changeBoxSort(int cbindex1, int cbindex2, ArrayList<BoxListData> list) {
        BoxListData boxListData = list.get(cbindex1);
        list.remove(cbindex1);
        list.add(cbindex2, boxListData);
    }


    public void getUrlListAsync(Callback<List<UrlListData>> callback) {
        mainServerInterface.getUrlListAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, callback);
    }
    public void postAddUrlAsync(String url, String name, String thumb, Callback<UrlListData> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.address = url;
        urlListData.urlname = name;
        urlListData.urlthumb = thumb;
        mainServerInterface.postAddUrlAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlListData, callback);
    }
    public void postRemoveUrlAsync(int urlid, Callback<Object> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.urlid = urlid;
        mainServerInterface.postRemoveUrlAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlListData, callback);
    }
    public void postEditUrlAsync(int urlid, String newname, Callback<UrlListData> callback) {
        UrlListData urlListData = new UrlListData();
        urlListData.urlid = urlid;
        urlListData.urlname = newname;
        mainServerInterface.postEditUrlAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlListData, callback);
    }
    public void getGoodAsync(int urlid, Callback<GoodData> callback) {
        mainServerInterface.getGoodAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlid, callback);
    }
    public void postAddGoodAsync(int urlid, Callback<Object> callback) {
        mainServerInterface.postAddGoodAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlid, callback);
    }
    public void postRemoveGoodAsync(int urlid, Callback<Object> callback) {
        mainServerInterface.postRemoveGoodAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, urlid, callback);
    }


    public void getUsrListAsync(Callback<List<UserData>> callback) {
        mainServerInterface.getUsrListAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, callback);
    }
    public void postAddUsrAsync(String usremail, String message, Callback<Object> callback) {
        TwoString twoString = new TwoString();
        twoString.usremail = usremail;
        twoString.message = message;
        mainServerInterface.postAddUsrAsync(LinkBoxController.userData.usrid, LinkBoxController.currentBox.cbid, twoString, callback);
    }


    public void postSignDownAsync(String pass, Callback<Object> callback) {
        UserData userData = new UserData();
        userData.usrid = LinkBoxController.userData.usrid;
        userData.usremail = LinkBoxController.userData.usremail;
        userData.pass = pass;
        mainServerInterface.postSignDownAsync(userData, callback);
    }


    public void postPushTokenAsync(String token, Callback<Object> callback) {
        mainServerInterface.postPushTokenAsync(LinkBoxController.userData.usrid, token, callback);
    }


    public void postPremium(Callback<Object> callback) {
        mainServerInterface.postPremium(LinkBoxController.userData, callback);
    }
}