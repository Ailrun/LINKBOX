package org.sopt.linkbox.custom.network;

import org.json.JSONObject;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface MainServerInterface {
//        public static final String API_KEY = "???"; // Maybe Not needed
String serverAPIEndPoint = "http://52.68.233.51:3000";
    @Headers("Content-Type: application/json")
    @POST("/usrList/login")
    void postLoginAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);
}
