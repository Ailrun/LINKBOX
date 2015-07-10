package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.data.LinkUrlListData;
import org.sopt.linkbox.custom.data.LinkUserData;
import org.sopt.linkbox.custom.data.TwoString;

import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.QueryMap;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class LinkNetworkInterface {
    public interface EmbedlyInterface {
        // for embedly api
        public static final String KEY = "7859e019f280493c89e030a41a135a79";

        @GET("/1/oembed")
        public void getDataAsync(@QueryMap HashMap<String, String> parameters, Callback<LinkNetwork.Embedly.ResultEmbedly> callback);
    }

    public interface MainServerInterface {
//        public static final String API_KEY = "???"; // Maybe Not needed
        @Headers("Content-Type: application/json")
        @POST("/usr/login")
        public void postLoginAsync(@Body LinkUserData linkUserData, Callback<LinkUserData> callback);

        @Headers("Content-Type: application/json")
        @POST("/usr/signup")
        public void postSignUpAsync(@Body LinkUserData linkUserData, Callback<LinkUserData> callback);

        @GET("/collectbox/{usrid}/boxlist")
        public void getBoxListAsync(@Path("usrid") int usrid, Callback<List<LinkBoxListData>> callback);

        @Headers("Content-Type: application/json")
        @POST("/collectbox/{usrid}/addbox")
        public void postAddBoxAsync(@Path("usrid") int usrid, @Body LinkBoxListData linkBoxListData, Callback<LinkBoxListData> callback);

        @Headers("Content-Type: application/json")
        @POST("/collectbox/{usrid}/removebox")
        public void postRemoveBoxAsync(@Path("usrid") int usrid, @Body LinkBoxListData linkBoxListData, Callback<Object> callback);

        @Headers("Content-Type: application/json")
        @POST("/collectbox/{usrid}/editbox")
        public void postEditBoxAsync(@Path("usrid") int usrid, @Body LinkBoxListData linkBoxListData, Callback<LinkBoxListData> callback);

        @Headers("Content-Type: application/json")
        @POST("/collecturl/{cbid}/urllist")
        public void postUrlListAsync(@Path("cbid") int cbid, @Body LinkUserData linkUserData, Callback<List<LinkUrlListData>> callback);

        @Headers("Content-Type: application/json")
        @POST("/collecturl/{usrid}/{cbid}/addurl")
        public void postAddUrlAsync(@Path("usrid") int usrid, @Path("cbid") int cbid, @Body LinkUrlListData linkUrlListData, Callback<LinkUrlListData> callback);

        @Headers("Content-Type: application/json")
        @POST("/collecturl/{cbid}/removeurl")
        public void postRemoveUrlAsync(@Path("cbid") int cbid, @Body LinkUrlListData linkUrlListData, Callback<Object> callback);

        @Headers("Content-Type: application/json")
        @POST("/collecturl/{cbid}/editurl")
        public void postEditUrlAsync(@Path("cbid") int cbid, @Body LinkUrlListData linkUrlListData, Callback<LinkUrlListData> callback);

        @Headers("Content-Type: application/json")
        @POST("/collecturl/{cbid}/{urlid}/editgood")
        public void postEditGoodAsync(@Path("cbid") int cbid, @Path("urlid") int urlid, @Body LinkUrlListData linkUrlListData, Callback<LinkUrlListData> callback);

        @GET("/share/{cbid}/usrlist")
        public void getUsrListAsync(@Path("cbid") int cbid, Callback<List<LinkUserData>> callback);

        @Headers("Content-Type: application/json")
        @POST("/share/{cbid}/addusr")
        public void postAddUsrAsync(@Path("cbid") int cbid, @Body TwoString values, Callback<Object> callback);

        @Headers("Content-Type: application/json")
        @POST("/usr/signdown")
        public void postSignDownAsync(@Body LinkUserData linkUserData, Callback<Object> callback);

        @Headers("Content-Type: application/json")
        @POST("/pushtoken/{usrid}")
        public void postPushTokenAsync(@Path("usrid") int usrid, @Body String token, Callback<Object> callback);

        @Headers("Content-Type: application/json")
        @POST("/premium")
        public void postPremium(@Body LinkUserData linkUserData, Callback<Object> callback);
    }
}















