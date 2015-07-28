package org.sopt.linkbox;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkEditorListAdapter;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.NotificationListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
import org.sopt.linkbox.custom.network.EmbedlyInterface;
import org.sopt.linkbox.custom.network.MainServerInterface;

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

    private EmbedlyInterface linkNetworkEmbedlyInterface;
    private MainServerInterface linkNetworkMainServerInterface;
    public EmbedlyInterface getLinkNetworkEmbedlyInterface() {
        return linkNetworkEmbedlyInterface;
    }
    public MainServerInterface getLinkNetworkMainServerInterface() {
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
        builderEmbedly.setEndpoint(EmbedlyInterface.embedlyAPIEndPoint);
        builderServer.setEndpoint(MainServerInterface.serverAPIEndPoint);
        builderEmbedly.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("key", EmbedlyInterface.KEY);
            }
        });

        builderEmbedly.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderServer.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderEmbedly.setClient(new OkClient(clientEmbedly));
        builderServer.setClient(new OkClient(clientServer));

        RestAdapter restAdapterEmbedly = builderEmbedly.build();
        RestAdapter restAdapterServer = builderServer.build();

        linkNetworkEmbedlyInterface = restAdapterEmbedly.create(EmbedlyInterface.class);
        linkNetworkMainServerInterface = restAdapterServer.create(MainServerInterface.class);

        currentBox = new BoxListData();

        boxListSource = new ArrayList<>();

        urlListSource = new ArrayList<>();

        editorListSource = new ArrayList<>();

        userData = new UserData();
    }

    public static ArrayList<BoxListData> boxListSource = null;
    public static LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;
    public static LinkItBoxListAdapter linkItBoxListAdapter = null;
    public static NotificationListAdapter notificationListAdapter = null;
    public static BoxEditBoxListAdapter boxEditBoxListAdapter = null;

    public static void notifyBoxDataSetChanged() {
        if (linkBoxBoxListAdapter != null) {
            linkBoxBoxListAdapter.notifyDataSetChanged();
        }
        if (linkItBoxListAdapter != null) {
            linkItBoxListAdapter.notifyDataSetChanged();
        }
        if (notificationListAdapter != null) {
            notificationListAdapter.notifyDataSetChanged();
        }
        if (boxEditBoxListAdapter != null) {
            boxEditBoxListAdapter.notifyDataSetChanged();
        }
    }


    public static BoxListData currentBox = null;

    public static ArrayList<UrlListData> urlListSource = null;
    public static LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    public static void notifyUrlDataSetChanged() {
        if (linkBoxUrlListAdapter != null) {
            linkBoxUrlListAdapter.notifyDataSetChanged();
        }
    }


    public static ArrayList<UserData> editorListSource = null;
    public static LinkEditorListAdapter linkEditorListAdapter = null;
    public static void notifyEditorDataSetChanged() {
        if (linkEditorListAdapter != null) {
            linkEditorListAdapter.notifyDataSetChanged();
        }
    }


    public static UserData userData = null;


    public static boolean defaultAlarm = false;
}
