package org.sopt.linkbox.custom.adapters;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by sjh on 2015-07-09.
 */
public class HttpClient {
    private static final String BASE_URL="http://192.168.219.105:52273/";

    private static AsyncHttpClient client=new AsyncHttpClient();

    public static AsyncHttpClient getInstance() {
        return HttpClient.client;
    }
    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }
    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL +relativeUrl;
    }
}