package org.sopt.linkbox;

import android.app.Application;

import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.network.MainServerInterface;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditInvitedBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkEditorListAdapter;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.NotificationListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.helper.Installation;
import org.sopt.linkbox.custom.network.AlarmListInterface;
import org.sopt.linkbox.custom.network.BoxListInterface;
import org.sopt.linkbox.custom.network.Embedly.EmbedlyInterface;
import org.sopt.linkbox.custom.network.UrlListInterface;
import org.sopt.linkbox.custom.network.UsrListInterface;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.ArrayList;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Junyoung on 2015-07-07.
 *
 */
public class LinkBoxController extends Application {
    private static LinkBoxController application;
    private static String applicationID;
    public static LinkBoxController getApplication() {
        return application;
    }
    public static String getApplicationID() {
        return applicationID;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        LinkBoxController.application = this;
        this.init();
    }

    private EmbedlyInterface linkNetworkEmbedlyInterface;
    private UsrListInterface usrListInterface;
    private BoxListInterface boxListInterface;
    private UrlListInterface urlListInterface;
    private AlarmListInterface alarmListInterface;
    public EmbedlyInterface getLinkNetworkEmbedlyInterface() {
        return linkNetworkEmbedlyInterface;
    }
    public UsrListInterface getUsrListInterface() {
        return usrListInterface;
    }
    public BoxListInterface getBoxListInterface() {
        return boxListInterface;
    }
    public UrlListInterface getUrlListInterface() {
        return urlListInterface;
    }
    public AlarmListInterface getAlarmListInterface() {
        return alarmListInterface;
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

    public static ArrayList<UsrListData> editorListSource = null;
    public static LinkEditorListAdapter linkEditorListAdapter = null;
    public static void notifyEditorDataSetChanged() {
        if (linkEditorListAdapter != null) {
            linkEditorListAdapter.notifyDataSetChanged();
        }
    }


    public static UsrListData usrListData = null;


    public static boolean defaultAlarm = false;


    private void initNetwork()
    {
        initNetworkServer();
    }
    private void initData()
    {
        currentBox = new BoxListData();

        boxListSource = new ArrayList<>();

        urlListSource = new ArrayList<>();

        editorListSource = new ArrayList<>();

        usrListData = new UsrListData();

        applicationID = Installation.id(this);
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

        usrListInterface = restAdapterServer.create(UsrListInterface.class);
        boxListInterface = restAdapterServer.create(BoxListInterface.class);
        urlListInterface = restAdapterServer.create(UrlListInterface.class);
        alarmListInterface = restAdapterServer.create(AlarmListInterface.class);
    }

/*    private void initNetworkEmbedly()
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
    }*/
}
