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

    @Headers("Content-Type: application/json")
    @POST("/usrList/signup")
    void postSignUpAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/usrList/facebook")
    void postFacebookAccessAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);




    @GET("/boxList/{usrKey}/boxList")
    void getBoxListAsync(@Path("usrKey") int usrKey, Callback<MainServerData<List<BoxListData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/addBox")
    void postAddBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<BoxListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/removeBox")
    void postRemoveBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/editBox")
    void postEditBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<BoxListData>> callback);

    @GET("/boxOfUsrList/{usrKey}/{boxKey}/boxUsrList")
    void getUsrListAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, Callback<MainServerData<List<UserData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxOfUsrList/{usrKey}/{boxKey}/addBoxUsr")
    void postAddUsrAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body TwoString values, Callback<MainServerData<Object>> callback);




    @GET("/urlList/{usrKey}/{boxKey}/boxUrlList")
    void getBoxUrlListAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, Callback<MainServerData<List<UrlListData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/addUrl")
    void postAddUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<UrlListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/removeUrl")
    void postRemoveUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/editUrl")
    void postEditUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<UrlListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/goodList/{usrKey}/{boxKey}/{urlKey}/addGood")
    void postAddGoodAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Path("urlKey") int urlid, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/goodList/{usrKey}/{boxKey}/{urlKey}/removeGood")
    void postRemoveGoodAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Path("urlKey") int urlid, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/usr/signdown")
    void postSignDownAsync(@Body UserData userData, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/push/register/{usrKey}")
    void postRegisterTokenAsync(@Path("usrKey") int usrKey, @Body JSONObject token, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/premium")
    void postPremium(@Body UserData userData, Callback<MainServerData<Object>> callback);
}
