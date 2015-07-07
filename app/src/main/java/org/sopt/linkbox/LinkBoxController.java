package org.sopt.linkbox;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.network.LinkNetworkInterface;

import java.net.CookieManager;
import java.net.CookiePolicy;

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
        builderServer.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
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
    }
}
