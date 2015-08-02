package org.sopt.linkbox;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditInvitedBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
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
        this.init();
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
        initNetwork();
        initData();
    }

    public static ArrayList<BoxListData> boxListSource = null;
    public static ArrayList<BoxListData> invitedBoxListSource = null;   // Added for invited box list
    public static LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;
    public static LinkItBoxListAdapter linkItBoxListAdapter = null;
    public static NotificationListAdapter notificationListAdapter = null;
    public static BoxEditBoxListAdapter boxEditBoxListAdapter = null;
    public static BoxEditInvitedBoxListAdapter boxEditInvitedBoxListAdapter = null; // Added for invited box list Adapter

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


    public static BoxListData currentBox = null;    // TODO : Current box must be filled whenever box is pressed
    // public static BoxListData currentInvitedBox = null;

    public static ArrayList<UrlListData> urlListSource = null;
    public static LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    // TODO : VERIFY DEPRECATED
    /*
    public static void notifyUrlDataSetChanged() {
        if (linkBoxUrlListAdapter != null) {
            linkBoxUrlListAdapter.notifyDataSetChanged();
        }
    }
    */

    public static ArrayList<UserData> editorListSource = null;
    public static LinkEditorListAdapter linkEditorListAdapter = null;
    public static void notifyEditorDataSetChanged() {
        if (linkEditorListAdapter != null) {
            linkEditorListAdapter.notifyDataSetChanged();
        }
    }


    public static UserData userData = null;


    public static boolean defaultAlarm = false;


    private void initNetwork()
    {
        initNetworkEmbedly();
        initNetworkServer();
    }
    private void initData()
    {
        currentBox = new BoxListData();

        boxListSource = new ArrayList<>();

        urlListSource = new ArrayList<>();

        editorListSource = new ArrayList<>();

        userData = new UserData();
    }

    private void initNetworkEmbedly()
    {
        CookieManager cookieManagerEmbedly = new CookieManager();
        cookieManagerEmbedly.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient clientEmbedly = new OkHttpClient();
        clientEmbedly.setCookieHandler(cookieManagerEmbedly);

        RestAdapter.Builder builderEmbedly = new RestAdapter.Builder();
        builderEmbedly.setEndpoint(EmbedlyInterface.embedlyAPIEndPoint);
        builderEmbedly.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addQueryParam("key", EmbedlyInterface.KEY);
            }
        });
        builderEmbedly.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderEmbedly.setClient(new OkClient(clientEmbedly));

        RestAdapter restAdapterEmbedly = builderEmbedly.build();

        linkNetworkEmbedlyInterface = restAdapterEmbedly.create(EmbedlyInterface.class);
    }
    private void initNetworkServer()
    {
        CookieManager cookieManagerServer = new CookieManager();
        cookieManagerServer.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        OkHttpClient clientServer = new OkHttpClient();
        clientServer.setCookieHandler(cookieManagerServer);

        RestAdapter.Builder builderServer = new RestAdapter.Builder();
        builderServer.setEndpoint(MainServerInterface.serverAPIEndPoint);
        builderServer.setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS);
        builderServer.setClient(new OkClient(clientServer));

        RestAdapter restAdapterServer = builderServer.build();

        linkNetworkMainServerInterface = restAdapterServer.create(MainServerInterface.class);
    }
}
