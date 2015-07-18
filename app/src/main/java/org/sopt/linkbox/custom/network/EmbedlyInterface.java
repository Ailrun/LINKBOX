package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.tempData.EmbedlyResult;

import java.util.HashMap;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.QueryMap;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface EmbedlyInterface {
    // for embedly api
    public static final String KEY = "7859e019f280493c89e030a41a135a79";

    public static final String embedlyAPIEndPoint = "http://api.embed.ly";

    @GET("/1/oembed")
    public void getThumbAsync(@QueryMap HashMap<String, String> parameters, Callback<EmbedlyResult> callback);
}
