package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.service.pushService.LinkRegistrationService;

import retrofit.Callback;

/**
 * Created by Junyoung on 2015-08-08.
 */
public class UsrListWrapper {
    private static UsrListInterface usrListInterface = null;

    private static void setUsrListInterface() {
        usrListInterface = LinkBoxController.getApplication().getUsrListInterface();
    }

    public UsrListWrapper() {
        setUsrListInterface();
    }

    public void login(String usrID, String usrPassword, int usrType, Callback<MainServerData<UsrListData>> callback) {
        UsrListData usrListData = new UsrListData();
        usrListData.usrID = usrID;
        usrListData.usrPassword = usrPassword;
        usrListData.usrType = usrType;
        usrListData.pushToken = LinkRegistrationService.getToken();
        usrListInterface.login(LinkBoxController.getApplicationID(), usrListData, callback);
    }
    public void signup(String usrID, String usrName, String usrPassword, int usrType, Callback<MainServerData<UsrListData>> callback) {
        UsrListData usrListData = new UsrListData();
        usrListData.usrID = usrID;
        usrListData.usrName = usrName;
        usrListData.usrPassword = usrPassword;
        usrListData.pushToken = LinkRegistrationService.getToken();
        usrListInterface.signup(LinkBoxController.getApplicationID(), usrListData, callback);
    }
    public void profile(String newProfile, Callback<MainServerData<Object>> callback) {
        UsrListData usrListData = new UsrListData();
        usrListData.usrKey = LinkBoxController.usrListData.usrKey;
        usrListData.usrID = LinkBoxController.usrListData.usrID;
        usrListData.usrProfile = newProfile;
        usrListInterface.profile(usrListData, callback);
    }
    public void passEdit(String newPassword, Callback<MainServerData<Object>> callback) {
        UsrListData usrListData = new UsrListData();
        usrListData.usrKey = LinkBoxController.usrListData.usrKey;
        usrListData.usrID = LinkBoxController.usrListData.usrID;
        usrListData.usrPassword = newPassword;
        usrListInterface.passEdit(usrListData, callback);
    }
    public void logout(Callback<MainServerData<Object>> callback) {
        usrListInterface.logout(LinkBoxController.getApplicationID(), LinkBoxController.usrListData, callback);
    }
    public void signdown(Callback<MainServerData<Object>> callback) {
        usrListInterface.signdown(LinkBoxController.usrListData, callback);
    }
}