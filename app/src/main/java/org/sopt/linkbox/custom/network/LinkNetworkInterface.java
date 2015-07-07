package org.sopt.linkbox.custom.network;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
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
        @GET("")
        Object getDataSync(@QueryMap HashMap<String, String> parameters);
        @GET("")
        Object getDataAsync(@QueryMap HashMap<String, String> parameters, Callback<Object> callback);
    }
}
