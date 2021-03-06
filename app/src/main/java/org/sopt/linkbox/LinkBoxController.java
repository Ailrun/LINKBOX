package org.sopt.linkbox;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.okhttp.OkHttpClient;

import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.AlarmListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.InvitedBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkEditorListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.NotificationListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.WebviewCommentListAdapter;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.mainData.url.CommentListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.helper.Installation;
import org.sopt.linkbox.custom.network.embedly.EmbedlyInterface;
import org.sopt.linkbox.custom.network.main.MainServerInterface;
import org.sopt.linkbox.custom.network.main.alarm.AlarmListInterface;
import org.sopt.linkbox.custom.network.main.box.BoxListInterface;
import org.sopt.linkbox.custom.network.main.search.SearchInterface;
import org.sopt.linkbox.custom.network.main.url.UrlListInterface;
import org.sopt.linkbox.custom.network.main.usr.UsrListInterface;
import org.sopt.linkbox.service.pushService.LinkRegistrationService;

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
    public static boolean inboxIndicator = false;

    // Google Analytics Tracker
    private Tracker mTracker = null;
    /*
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
    */

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    public void onCreate() {
        super.onCreate();
        LinkBoxController.application = this;
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);

            mTracker.enableAutoActivityTracking(true);
        }
        init();
    }
    //</editor-fold>

    //<editor-fold desc="No Use" defaultstate="collapsed">
    private EmbedlyInterface linkNetworkEmbedlyInterface;
    //</editor-fold>
    //<editor-fold desc="Private Interfaces" defaultstate="collapsed">
    private UsrListInterface usrListInterface;
    private BoxListInterface boxListInterface;
    private UrlListInterface urlListInterface;
    private AlarmListInterface alarmListInterface;
    private SearchInterface searchInterface;
    //</editor-fold>
    //<editor-fold desc="No Use" defaultstate="collapsed">
    public EmbedlyInterface getLinkNetworkEmbedlyInterface() {
        return linkNetworkEmbedlyInterface;
    }
    //</editor-fold>
    //<editor-fold desc="Interface Getters" defaultstate="collapsed">
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
    public SearchInterface getSearchInterface(){
        return searchInterface;
    }
    //</editor-fold>

    private void init() {
        initNetwork();
        initData();
    }

    public static ArrayList<AlarmListData> alarmBoxListSource = null;   // Added for invited box list
    public static InvitedBoxListAdapter invitedBoxListAdapter = null; // Added for invited box list Adapter
    public static AlarmListAdapter alarmListAdapter = null;

    public static void notifyAlarmDataSetChanged() {
        if (invitedBoxListAdapter != null) {
            invitedBoxListAdapter.notifyDataSetChanged();
        }
        if (alarmListAdapter != null) {
            alarmListAdapter.notifyDataSetChanged();
        }
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


    public static UsrListData usrListData = null;
    // Code for user profile image cropping
    // public static Bitmap temporaryImage = null;
    public static Bitmap userImage = null;
    public static Bitmap boxImage = null;

    public static BoxListData currentBox = null;    // TODO : Current box must be filled whenever box is pressed

    public static int alarmCount = 0;


    public static boolean defaultAlarm = false;
    public static boolean defaultReadLater = false;

    public static boolean new_link_alarm = false;
    public static boolean invited_box_alarm = false;
    public static boolean like_alarm = false;
    public static boolean comment_alarm = false;

    public static int preference_readLater = 2;

    public static ArrayList<UrlListData> urlListSource = null;
    public static LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;

    public static void notifyUrlDataSetChanged() {
        if (linkBoxUrlListAdapter != null) {
            linkBoxUrlListAdapter.notifyDataSetChanged();
        }
        if(webviewCommentListAdapter != null)
        {
            webviewCommentListAdapter.notifyDataSetChanged();
        }

    }
    public static void resetUrlDataSet() {
        if (linkBoxUrlListAdapter != null) {
            linkBoxUrlListAdapter.closeAllItems();
        }
    }


    public static ArrayList<CommentListData> commentListSource = null;
    public static WebviewCommentListAdapter webviewCommentListAdapter = null;

    public static void notifyCommentDataSetChanged() {
        if (webviewCommentListAdapter != null) {
            webviewCommentListAdapter.notifyDataSetChanged();
            Log.d("TEST", "aaa");
        }
    }


    public static ArrayList<UsrListData> editorListSource = null;
    public static LinkEditorListAdapter linkEditorListAdapter = null;
    public static void notifyEditorDataSetChanged() {
        if (linkEditorListAdapter != null) {
            linkEditorListAdapter.notifyDataSetChanged();
        }
    }


    /*
    * TODO : When Alarm List is added, Plz Control InvitedBoxListAdapter
    *
    * TODO : PLZZZZZZZ
    */


    private void initNetwork() {
        initGcm();
        initNetworkServer();
    }
    private void initData() {
        applicationID = Installation.id(this);

        currentBox = new BoxListData();


        boxListSource = new ArrayList<>();
        urlListSource = new ArrayList<>();
        editorListSource = new ArrayList<>();
        alarmBoxListSource = new ArrayList<>();
        commentListSource = new ArrayList<>();
    }
    private void initGcm() {
        if (isGoogleServiceAvailable()) {
            Intent intent = new Intent(this, LinkRegistrationService.class);
            startService(intent);
        }
    }
    private void initNetworkServer() {
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
        searchInterface = restAdapterServer.create(SearchInterface.class);
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private boolean isGoogleServiceAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(getApplicationContext(), "This device does not support Google Play Service", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */

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
