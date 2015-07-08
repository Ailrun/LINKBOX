package org.sopt.linkbox;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.adapters.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.adapters.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.data.LinkUrlListData;
import org.sopt.linkbox.custom.data.LinkUserData;
import org.sopt.linkbox.custom.network.LinkNetworkInterface;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class LinkBoxController extends Application {
    private static LinkBoxController application;
    public static LinkBoxController getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LinkBoxController.application = this;
        LinkBoxController.application.init();
    }

    private LinkNetworkInterface.EmbedlyInterface linkNetworkEmbedlyInterface;
    private LinkNetworkInterface.MainServerInterface linkNetworkMainServerInterface;
    public LinkNetworkInterface.EmbedlyInterface getLinkNetworkEmbedlyInterface() {
        return linkNetworkEmbedlyInterface;
    }
    public LinkNetworkInterface.MainServerInterface getLinkNetworkMainServerInterface() {
        return linkNetworkMainServerInterface;
    }

    private void init() {
        CookieManager cookieManagerEmbedly = new CookieManager();
        CookieManager cookieManagerServer = new CookieManager();
        cookieManagerEmbedly.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        cookieManagerServer.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        OkHttpClient clientEmbedly = new OkHttpClient();
        OkHttpClient clientServer = new OkHttpClient();
        clientEmbedly.setCookieHandler(cookieManagerEmbedly);
        clientServer.setCookieHandler(cookieManagerServer);

        RestAdapter.Builder builderEmbedly = new RestAdapter.Builder();
        RestAdapter.Builder builderServer = new RestAdapter.Builder();
        builderEmbedly.setEndpoint("http://api.embed.ly");
        builderServer.setEndpoint("http://linkbox.server");
        builderEmbedly.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("key", LinkNetworkInterface.EmbedlyInterface.KEY);
            }
        });

        builderEmbedly.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderServer.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderEmbedly.setClient(new OkClient(clientEmbedly));
        builderServer.setClient(new OkClient(clientServer));

        RestAdapter restAdapterEmbedly = builderEmbedly.build();
        RestAdapter restAdapterServer = builderServer.build();

        linkNetworkEmbedlyInterface = restAdapterEmbedly.create(LinkNetworkInterface.EmbedlyInterface.class);
        linkNetworkMainServerInterface = restAdapterServer.create(LinkNetworkInterface.MainServerInterface.class);

        boxListSource = new ArrayList<>();
        linkBoxBoxListAdapter =
                new LinkBoxBoxListAdapter(getApplicationContext(), boxListSource);
        linkItBoxListAdapter =
                new LinkItBoxListAdapter(getApplicationContext(), boxListSource);

        urlListSource = new ArrayList<>();
        linkBoxUrlListAdapter =
                new LinkBoxUrlListAdapter(getApplicationContext(), urlListSource);

        linkUserData = new LinkUserData();
    }

    public static ArrayList<LinkBoxListData> boxListSource = null;

    public static LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;
    public static LinkItBoxListAdapter linkItBoxListAdapter = null;
    public static void notifyBoxDataSetChanged() {
        linkBoxBoxListAdapter.notifyDataSetChanged();
        linkItBoxListAdapter.notifyDataSetChanged();
    }

    public static int currentBox = 0;

    public static ArrayList<LinkUrlListData> urlListSource = null;

    public static LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    public static void notifyUrlDataSetChanged() {
        linkBoxUrlListAdapter.notifyDataSetChanged();
    }

    public static LinkUserData linkUserData = null;
}
