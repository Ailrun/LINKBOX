package org.sopt.linkbox.custom.network;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
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
        Object getDataSync(@QueryMap HashMap<String, String> parameters);
        @GET("/1/oembed")
        Object getDataAsync(@QueryMap HashMap<String, String> parameters, Callback<Object> callback);
    }

    public interface MainServerInterface {
//        public static final String API_KEY = "???"; // Maybe Not needed
        @POST("/usr/login")
        Object postLoginSync(@QueryMap HashMap<String, String> parameters);
        @POST("/usr/login")
        Object postLoginAsync(@QueryMap HashMap<String, String> parameters);
        @POST("/usr/signup")
        Object postSignUpSync(@QueryMap HashMap<String, String> parameters);
        @POST("/usr/signup")
        Object postSignUpAsync(@QueryMap HashMap<String, String> parameters);
        @GET("/collectbox/{usrid}/boxlist")
        Object getBoxListSync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @GET("/collectbox/{usrid}/boxlist")
        Object getBoxListAsync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters, Callback<Object> callback);
        @POST("/collectbox/{usrid}/addbox")
        Object postAddBoxSync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @POST("/collectbox/{usrid}/removebox")
        Object postRemoveBoxSync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @POST("/collectbox/{usrid}/removebox")
        Object postRemoveBoxAsync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @POST("/collectbox/{usrid}/editbox")
        Object postEditBoxSync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @POST("/collectbox/{usrid}/editbox")
        Object postEditBoxAsync(@Path("usrid") String usrid, @QueryMap HashMap<String, String> parameters);
        @GET("/collecturl/{cbid}/urllist")
        Object getUrlListSync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @GET("/collecturl/{cbid}/urllist")
        Object getUrlListAsync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/addurl")
        Object postAddUrlSync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/addurl")
        Object postAddUrlAsync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/removeurl")
        Object postRemoveUrlSync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/removeurl")
        Object postRemoveUrlAsync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/editurl")
        Object postEditUrlSync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/editurl")
        Object postEditUrlAsync(@Path("cbid") String cbid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/{urlid}/editgood")
        Object postEditGoodSync(@Path("cbid") String cbid, @Path("urlid") String urlid, @QueryMap HashMap<String, String> parameters);
        @POST("/collecturl/{cbid}/{urlid}/editgood")
        Object postEditGoodAsync(@Path("cbid") String cbid, @Path("urlid") String urlid, @QueryMap HashMap<String, String> parameters);
        @POST("/usr/signdown")
        Object postSignDownSync(@QueryMap HashMap<String, String> parameters);
        @POST("/usr/signdown")
        Object postSignDownAsync(@QueryMap HashMap<String, String> parameters);
    }
}
