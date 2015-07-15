package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.GoodData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
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
    @Headers("Content-Type: application/json")
    @POST("/usr/login")
    public void postLoginAsync(@Body UserData userData, Callback<UserData> callback);

    @Headers("Content-Type: application/json")
    @POST("/usr/signup")
    public void postSignUpAsync(@Body UserData userData, Callback<UserData> callback);




    @GET("/collectbox/{usrid}/boxlist")
    public void getBoxListAsync(@Path("usrid") int usrid, Callback<List<BoxListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/collectbox/{usrid}/addbox")
    public void postAddBoxAsync(@Path("usrid") int usrid, @Body BoxListData boxListData, Callback<BoxListData> callback);

    @Headers("Content-Type: application/json")
    @POST("/collectbox/{usrid}/removebox")
    public void postRemoveBoxAsync(@Path("usrid") int usrid, @Body BoxListData boxListData, Callback<Object> callback);

    @Headers("Content-Type: application/json")
    @POST("/collectbox/{usrid}/editbox")
    public void postEditBoxAsync(@Path("usrid") int usrid, @Body BoxListData boxListData, Callback<BoxListData> callback);




    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/urllist")
    public void postUrlListAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, Callback<List<UrlListData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/addurl")
    public void postAddUrlAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Body UrlListData urlListData, Callback<UrlListData> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/removeurl")
    public void postRemoveUrlAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Body UrlListData urlListData, Callback<Object> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/editurl")
    public void postEditUrlAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Body UrlListData urlListData, Callback<UrlListData> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/{urlid}/good")
    public void getGoodAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Path("urlid") int urlid, Callback<GoodData> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/{urlid}/addgood")
    public void postAddGoodAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Path("urlid") int urlid, Callback<Object> callback);

    @Headers("Content-Type: application/json")
    @POST("/collecturl/{usrid}/{cbid}/{urlid}/removegood")
    public void postRemoveGoodAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Path("urlid") int urlid, Callback<Object> callback);




    @GET("/share/{usrid}/{cbid}/usrlist")
    public void getUsrListAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, Callback<List<UserData>> callback);

    @Headers("Content-Type: application/json")
    @POST("/share/{usrid}/{cbid}/addusr")
    public void postAddUsrAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Body TwoString values, Callback<Object> callback);




    @Headers("Content-Type: application/json")
    @POST("/usr/signdown")
    public void postSignDownAsync(@Body UserData userData, Callback<Object> callback);




    @Headers("Content-Type: application/json")
    @POST("/pushtoken/{usrid}")
    public void postPushTokenAsync(@Path("usrid") int usrid, @Body String token, Callback<Object> callback);




    @Headers("Content-Type: application/json")
    @POST("/premium")
    public void postPremium(@Body UserData userData, Callback<Object> callback);
}
