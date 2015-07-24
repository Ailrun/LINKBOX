package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.GoodData;
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
    public static final String serverAPIEndPoint = "http://52.68.233.51:3000";
    @Headers("Content-Type: application/json")
    @POST("/usrList/login")
    public void postLoginAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/usrList/signup")
    public void postSignUpAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/usrList/facebook")
    public void postFacebookAccessAsync(@Body UserData userData, Callback<MainServerData<UserData>> callback);




    @GET("/boxList/{usrKey}/boxList")
    public void getBoxListAsync(@Path("usrKey") int usrKey, Callback<MainServerData<List<BoxListData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/addBox")
    public void postAddBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<BoxListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/removeBox")
    public void postRemoveBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxList/{usrKey}/editBox")
    public void postEditBoxAsync(@Path("usrKey") int usrKey, @Body BoxListData boxListData, Callback<MainServerData<BoxListData>> callback);

    @GET("/boxOfUsrList/{usrKey}/{boxKey}/boxUsrList")
    public void getUsrListAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, Callback<MainServerData<List<UserData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/boxOfUsrList/{usrKey}/{boxKey}/addBoxUsr")
    public void postAddUsrAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body TwoString values, Callback<MainServerData<Object>> callback);




    @GET("/urlList/{usrKey}/{boxKey}/urlList")
    public void getUrlListAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, Callback<MainServerData<List<UrlListData>>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/addUrl")
    public void postAddUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<UrlListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/removeUrl")
    public void postRemoveUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/urlList/{usrKey}/{boxKey}/editUrl")
    public void postEditUrlAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Body UrlListData urlListData, Callback<MainServerData<UrlListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/goodList/{usrKey}/{boxKey}/{urlKey}/addGood")
    public void postAddGoodAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Path("urlKey") int urlid, Callback<MainServerData<Object>> callback);

    @Headers("Content-Type: application/json")
    @POST("/goodList/{usrKey}/{boxKey}/{urlKey}/removeGood")
    public void postRemoveGoodAsync(@Path("usrKey") int usrKey, @Path("boxKey") int boxKey, @Path("urlKey") int urlid, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/usr/signdown")
    public void postSignDownAsync(@Body UserData userData, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/pushtoken/{usrKey}")
    public void postPushTokenAsync(@Path("usrKey") int usrKey, @Body String token, Callback<MainServerData<Object>> callback);




    @Headers("Content-Type: application/json")
    @POST("/premium")
    public void postPremium(@Body UserData userData, Callback<MainServerData<Object>> callback);
}
