package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.accountPage.AccountActivity;
import org.sopt.linkbox.activity.alarmPage.AlarmActivity;
import org.sopt.linkbox.activity.helpPage.HelpActivity;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxListEditActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.WebviewActivity;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorList;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.network.main.usr.UsrListWrapper;
import org.sopt.linkbox.custom.widget.RoundedImageView;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.ImageSaveLoad;
import org.sopt.linkbox.custom.helper.SessionSaver;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;
import org.sopt.linkbox.libUtils.util.IabHelper;
import org.sopt.linkbox.libUtils.util.IabResult;
import org.sopt.linkbox.libUtils.util.Inventory;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-06-30.
 *
 */

/** T?O?D?O : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
public class LinkBoxActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LinkBoxActivity.class.getName() + " : ";
    private static final int RESULT_HELP = 1;

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private LayoutInflater layoutInflater = null;

    private UrlListWrapper urlListWrapper = null;

    private String boxTitle = null;

    //toolbar layout
    private Toolbar tToolbar = null;
    private MenuItem menuItems[] = null;
    //main layout
    private SwipeRefreshLayout srlUrlList = null;
    private ListView lvUrlList = null;
    private LinearLayout llUrlHeader = null;
    private TextView tvBoxTitle = null;
    private TextView tvUrlNum = null;
    private LinearLayout llUrlEmptyView = null;
    //drawer layout
    private RoundedImageView ivProfile = null;
    private ListView lvFavoriteBoxList = null;

    private TextView tvUserName = null;
    private TextView tvUserEmail = null;

    private RelativeLayout rlRecentLink = null;
    private TextView tvRecentLinkText = null;
    private RelativeLayout rlMyBox = null;
    private TextView tvMyBoxText = null;
    private RelativeLayout rlBuyedBox = null;

    private RelativeLayout rlToSetting = null;
    private TextView tvToSettingText = null;
    private RelativeLayout rlToHelp = null;
    private TextView tvToHelpText = null;

    private DrawerLayout dlDrawer = null;
    private ActionBarDrawerToggle abdtDrawer = null;

    private IabHelper iabHelper = null;
    private String base64EncodedPublicKey = null;
    private final String skuIDPremium = "id_mUImErpEmEkAMU";
    private List<String> skuList = null;

    private ImageButton ibDeleteLinkBox = null;
    private ImageButton ibEditLinkBox = null;
    private ImageButton ibShareLinkBox = null;
    // Load Save
    private ImageSaveLoad imageSaveLoader = null;
    private SharedPreferences prefs = null;


    private SharedPreferences spProfile;
    private SharedPreferences.Editor speProfile;
    private UsrListWrapper usrListWrapper = null;

    private boolean afterphoto = false;

    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num=" + LinkBoxController.urlListSource.size());

        if(LinkBoxController.usrListData == null){
            Toast.makeText(LinkBoxActivity.this, "잘못된 접근입니다.", Toast.LENGTH_SHORT).show();
            usrListWrapper = new UsrListWrapper();

            spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
            speProfile = spProfile.edit();

            speProfile.remove(AccountStrings.usrID);
            speProfile.remove(AccountStrings.usrPassword);
            LoginManager.getInstance().logOut();
            usrListWrapper.logout(new LogoutCallback());
        }

        initPreference();
        initInterface();
        initData();
        initView();
        initControl();
        initListener();
        initInBox();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(afterphoto)
            afterphoto = false;
        else
           dlDrawer.closeDrawers();
          
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (LinkBoxController.userImage != null) {
            ivProfile.setImageBitmap(LinkBoxController.userImage);
            String saveStatus = imageSaveLoader.saveProfileImage(LinkBoxController.userImage);
            Log.d("Save Status : ", saveStatus);
        }
        LinkBoxController.resetUrlDataSet();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d(TAG, "In NewIntent : " + LinkBoxController.currentBox.toString());
        Log.d(TAG, "inBox? : " + getIntent().getBooleanExtra(MainStrings.inBox, false));
        LinkBoxController.inboxIndicator = getIntent().getBooleanExtra(MainStrings.inBox, false);
        initInBox();
        invalidateOptionsMenu();
        if (LinkBoxController.urlListSource.size() > 0) {
            int index = 0;
            int getIndex = getIntent().getIntExtra(MainStrings.urlKey, LinkBoxController.urlListSource.get(0).urlKey);
            for (UrlListData u : LinkBoxController.urlListSource) {
                index = u.urlKey == getIndex ? LinkBoxController.urlListSource.indexOf(u) : 0;
            }
            lvUrlList.smoothScrollToPosition(index);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        initInBox();
        invalidateOptionsMenu();
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        abdtDrawer.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abdtDrawer.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link_box, menu);
        menuItems = new MenuItem[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            menuItems[i] = menu.getItem(i);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItems[1].setVisible(!LinkBoxController.inboxIndicator);
        menuItems[2].setVisible(LinkBoxController.inboxIndicator);
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abdtDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));

                break;
            case R.id.action_alarms:
                startActivity(new Intent(this, AlarmActivity.class));

                break;
            case R.id.action_editors:
                startActivity(new Intent(this, BoxEditorList.class));

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onStop() {
        super.onStop();
        SessionSaver.saveSession(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) {
            iabHelper.dispose();
        }
        iabHelper = null;
        //        if (spProfile.getBoolean("floating", true)) {
        //            startService(new Intent(getApplicationContext(), LinkHeadService.class));
        //        }
    }

    @Override
    public void onBackPressed() {
        if (LinkBoxController.inboxIndicator) {
            Intent intent = new Intent(LinkBoxActivity.this, BoxListEditActivity.class);
            startActivity(intent);

            invalidateOptionsMenu();
        }
        else {
            Intent intent = new Intent(LinkBoxActivity.this, LinkBoxActivity.class);
            startActivity(intent);
            finish();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initPreference() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (prefs != null) {
            LinkBoxController.preference_readLater = prefs.getInt("read_later_preference", 2);
            int defaultAlarmIndicator = prefs.getInt("alarm_enable", 0);
            int defaultReadLaterIndicator = prefs.getInt("read_later_enable", 0);
            int new_link_alarm = prefs.getInt("new_link_alarm", 0);
            int invited_box_alarm = prefs.getInt("invited_box_alarm", 0);
            int like_alarm = prefs.getInt("like_alarm", 0);
            int comment_alarm = prefs.getInt("comment_alarm", 0);


            if (defaultAlarmIndicator == 1) {
                LinkBoxController.defaultAlarm = true;
            }
            else if (defaultAlarmIndicator == 0) {
                LinkBoxController.defaultAlarm = false;
            }
            if (defaultReadLaterIndicator == 1) {
                LinkBoxController.defaultReadLater = true;
            }
            else if (defaultReadLaterIndicator == 0) {
                LinkBoxController.defaultReadLater = false;
            }
            if (new_link_alarm == 1) {
                LinkBoxController.new_link_alarm = true;
            }
            else if (new_link_alarm == 0) {
                LinkBoxController.new_link_alarm = false;
            }
            if (invited_box_alarm == 1) {
                LinkBoxController.invited_box_alarm = true;
            }
            else if (invited_box_alarm == 0) {
                LinkBoxController.invited_box_alarm = false;
            }
            if (like_alarm == 1) {
                LinkBoxController.like_alarm = true;
            }
            else if (like_alarm == 0) {
                LinkBoxController.like_alarm = false;
            }
            if (comment_alarm == 1) {
                LinkBoxController.comment_alarm = true;
            }
            else if (comment_alarm == 0) {
                LinkBoxController.comment_alarm = false;
            }
        }
    }

    private void initInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    private void initData() {
        //InApp billing init
        // initInAppPayData();

        //Profile data
        imageSaveLoader = new ImageSaveLoad(getApplicationContext());
        LinkBoxController.userImage = imageSaveLoader.loadProfileImage();

        //Page Data
        LinkBoxController.inboxIndicator = getIntent().getBooleanExtra(MainStrings.inBox, false);

        //other data init;
        //initUrlDummyData();
        //initBoxDummyData();

    }
    private void initView() {
        layoutInflater = getLayoutInflater();

        //toolbar init
        initToolbarView();

        //main init
        initMainView();

        //drawer init
        initDrawerView();
        // initDrawerButtonHeaderView();
        // initDrawerEditHeaderView();


    }
    private void initListener() {
        //InApp billing init
        // initInAppPayListener();

        //main init
        initMainListener();
        initDrawerTouchListener();
        //drawer init
        initDrawerListener();
        // initDrawerButtonHeaderListener();
        // initDrawerEditHeaderListener();
    }
    private void initControl() {
        //TODO : Change To FavoriteBox's Adapter
        LinkBoxController.linkBoxBoxListAdapter =
                new LinkBoxBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        LinkBoxController.linkBoxUrlListAdapter =
                new LinkBoxUrlListAdapter(getApplicationContext(), LinkBoxController.urlListSource);

        lvUrlList.setAdapter(LinkBoxController.linkBoxUrlListAdapter);
        lvFavoriteBoxList.setAdapter(LinkBoxController.linkBoxBoxListAdapter);
    }
    //</editor-fold>
    //<editor-fold desc="Initiate InAppPays" defaultstate="collapsed">
    private void initInAppPayData() {
        base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiVdcBxJfbqtVYooV";
        base64EncodedPublicKey += "X8zI/i9FxWgmq2UYDDmaSAl3CKaB/1z4RusVD8pKVkHjumWFZ0OFyBPDc3ku";
        base64EncodedPublicKey += "nFxjh5gGKUvDTdCjdAK2SCPHuW0PNb6fbydRX6i8gmq9sDZq+acy4gq2JEa0";
        base64EncodedPublicKey += "lmKIR0KWP6meKP7kjZ5WOGqIuRUcDXYfit5OpdTFMqaVuqousWgpYS0y0SJo";
        base64EncodedPublicKey += "iSwGPQOGjG+v7gpH11WO45aDlmKzEb3sgyApU+lGYz9ekrIpPFolT9wH0+MR";
        base64EncodedPublicKey += "JHvReqFs5jyKPZMIoryk8lCscGpYtUIjFFi8lnyZ7JRaXSwV0X4AvUqJBAIf";
        base64EncodedPublicKey += "sh2YwAptVBGkdmcjsjWw7MvCmwIDAQAB";
        iabHelper = new IabHelper(this, base64EncodedPublicKey);
        skuList = new ArrayList<>();
        skuList.add(skuIDPremium);
    }
    private void initInAppPayListener() {
        iabHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    //T?O?D?O : If Setup Fail, HANDLE ERROR (like Toast)
                    Log.d(TAG, "Problem setting up In-app Billing: " + result);
                }

                iabHelper.queryInventoryAsync(true, skuList, new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                        if (!result.isSuccess()) {
                            //T?O?D?O : HANDLE ERROR (like Toast)
                        }
                        String premiumPrice = inv.getSkuDetails(skuIDPremium).getPrice();
                        //T?O?D?O : UI UPDATE
                    }
                });
            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Toolbars" defaultstate="collapsed">
    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setNavigationIcon(R.drawable.ic_box_add);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
        //After instantiating your ActionBarDrawerToggle

        setSupportActionBar(tToolbar);
    }

    //</editor-fold>
    //<editor-fold desc="Initiate Mains" defaultstate="collapsed">
    private void initMainView() {
        srlUrlList = (SwipeRefreshLayout) findViewById(R.id.SRL_url_list_link_box);
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
        llUrlHeader = (LinearLayout) layoutInflater.inflate(R.layout.layout_header_url_list_link_box, lvUrlList, false);
        tvBoxTitle = (TextView) llUrlHeader.findViewById(R.id.TV_box_title_link_box);
        tvUrlNum = (TextView) llUrlHeader.findViewById(R.id.TV_url_number_link_box);
        //        ViewGroup viewGroup = (ViewGroup) lvUrlList.getParent();
        //        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, viewGroup, false);
        //        viewGroup.addView(llUrlEmptyView);
        //        lvUrlList.setEmptyView(llUrlEmptyView);
    }
    private void initMainListener() {
        srlUrlList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LinkBoxController.linkBoxUrlListAdapter.closeAllItems();
                initInBox();
            }
        });
        lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(LinkBoxActivity.this, WebviewActivity.class);
                        if (LinkBoxController.inboxIndicator) {
                    if(position == 0) {
                        return;
                    }
                    intent.putExtra(MainStrings.position, position-1);
                }
                else {
                    intent.putExtra(MainStrings.position, position);
                }
                startActivity(intent);

            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Drawers" defaultstate="collapsed">
    private void initDrawerView() {
        ivProfile = (RoundedImageView) findViewById(R.id.RIV_profile_link_box);
        // tvBoxNumber = (TextView) findViewById(R.id.TV_box_number_link_box);
        tvUserName = (TextView) findViewById(R.id.TV_profile_name_link_box);
        tvUserEmail = (TextView) findViewById(R.id.TV_profile_email_link_box);

        // * Drawer Layout
        // ** RecentLink layout
        rlRecentLink = (RelativeLayout) findViewById(R.id.RL_recent_link_link_box);
        tvRecentLinkText = (TextView) findViewById(R.id.TV_recent_link_box);
        // ** MyBox Layout
        rlMyBox = (RelativeLayout) findViewById(R.id.RL_my_box_link_box);
        tvMyBoxText = (TextView) findViewById(R.id.TV_my_box_link_box);
        rlBuyedBox = (RelativeLayout) findViewById(R.id.RL_buyed_box);

        lvFavoriteBoxList = (ListView) findViewById(R.id.LV_favorite_box_link_box);
        lvFavoriteBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dlDrawer = (DrawerLayout) findViewById(R.id.DL_root_layout_link_box);
        // ** ToSetting Layout
        rlToSetting = (RelativeLayout) findViewById(R.id.RL_setting_link_box);
        tvToSettingText = (TextView) findViewById(R.id.TV_setting_link_box);
        // ** ToHelp Layout
        rlToHelp = (RelativeLayout) findViewById(R.id.RL_help_link_box);
        tvToHelpText = (TextView) findViewById(R.id.TV_help_link_box);

        ibDeleteLinkBox = (ImageButton) findViewById(R.id.IB_delete_link_box);
        ibEditLinkBox = (ImageButton) findViewById(R.id.IB_edit_link_box);
        ibShareLinkBox = (ImageButton) findViewById(R.id.IB_share_link_box);
    }

    private void initDrawerTouchListener(){
        rlRecentLink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rlRecentLink.setBackgroundResource(R.drawable.custom_drawer_selected);
                    tvRecentLinkText.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    rlRecentLink.setBackgroundResource(R.drawable.custom_drawer_unselected);
                    tvRecentLinkText.setTextColor(getResources().getColor(R.color.real_black));
                }

                return false;
            }
        });
        rlMyBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rlMyBox.setBackgroundResource(R.drawable.custom_drawer_selected);
                    tvMyBoxText.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    rlMyBox.setBackgroundResource(R.drawable.custom_drawer_unselected);
                    tvMyBoxText.setTextColor(getResources().getColor(R.color.real_black));
                }

                return false;
            }
        });
        rlToSetting.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rlToSetting.setBackgroundResource(R.drawable.custom_drawer_selected);
                    tvToSettingText.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    rlToSetting.setBackgroundResource(R.drawable.custom_drawer_unselected);
                    tvToSettingText.setTextColor(getResources().getColor(R.color.real_black));
                }

                return false;
            }
        });
        rlToHelp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    rlToHelp.setBackgroundResource(R.drawable.custom_drawer_selected);
                    tvToHelpText.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    rlToHelp.setBackgroundResource(R.drawable.custom_drawer_unselected);
                    tvToHelpText.setTextColor(getResources().getColor(R.color.real_black));
                }

                return false;
            }
        });
        /*
        lvFavoriteBoxList.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(lvFavoriteBoxList.getSelectedView() == v){
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        rlToHelp.setBackgroundResource(R.drawable.custom_drawer_selected);
                        tvToHelpText.setTextColor(getResources().getColor(R.color.real_white));
                    } else {

                    }
                }

                return false;
            }
        });
        */
    }
    private void initDrawerListener() {
        tvUserName.setText(LinkBoxController.usrListData.usrName);
        tvUserEmail.setText(LinkBoxController.usrListData.usrID);

        rlRecentLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinkBoxController.inboxIndicator = false;
                initInBox();
                invalidateOptionsMenu();
                dlDrawer.closeDrawers();
            }
        });
        rlMyBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LinkBoxActivity.this, BoxListEditActivity.class);
                startActivity(intent);

            }
        });
        rlBuyedBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "BuyedBox Clicked");
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterphoto = true;
                Intent intent = new Intent(LinkBoxActivity.this, PhotoCropActivity.class);
                startActivity(intent);

            }
        });
        lvFavoriteBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinkBoxController.currentBox = (BoxListData) adapterView.getItemAtPosition(i);
                LinkBoxController.inboxIndicator = true;
                initInBox();
                invalidateOptionsMenu();
                dlDrawer.closeDrawers();
            }
        });

        abdtDrawer = new ActionBarDrawerToggle(this, dlDrawer,
                                               R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        dlDrawer.setDrawerListener(abdtDrawer);
        rlToSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserSettingActivity.class));

            }
        });
        rlToHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), HelpActivity.class), RESULT_HELP);

            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate InBox" defaultstate="collapsed">
    private void initInBox() {
        if (LinkBoxController.inboxIndicator) {
            ifInBox();
        }
        else {
            elseInBox();
        }
    }
    //</editor-fold>

    //<editor-fold desc="Inbox Submethods" defaultstate="collapsed">
    private void ifInBox() {
        if (LinkBoxController.currentBox != null) {
            urlListWrapper.boxList(0, 200, new UrlLoading());
            boxTitle = LinkBoxController.currentBox.boxName;
            tToolbar.setTitle("");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("");
            }
            tvBoxTitle.setText(boxTitle);
            if (lvUrlList.getHeaderViewsCount() == 0) {
                lvUrlList.addHeaderView(llUrlHeader);
            }
            lvUrlList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }
                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    View v = lvUrlList.getChildAt(0);
                    int top = (v == null) ? 0 : v.getTop();
                    final int titleSize = 50;
                    tvBoxTitle.setAlpha((firstVisibleItem != 0 || v == null || v.getHeight() == 0) ? 1.0f : (1.0f + ((float) top) / titleSize));
                    if (top < -titleSize || firstVisibleItem != 0) {
                        tToolbar.setTitle(boxTitle);
                    }
                    else {
                        tToolbar.setTitle("");
                    }
                }
            });
            lvUrlList.setSelection(0);
            srlUrlList.setProgressViewOffset(true, 80, 150);
            srlUrlList.setColorSchemeResources(R.color.indigo500);
        }
        else {
            Log.e(TAG, "ERROR!!! inBox=" + LinkBoxController.inboxIndicator + " and currentBox=null");
        }
    }
    private void elseInBox() {
        urlListWrapper.allList(0, 200, new UrlLoading());
        tToolbar.setTitle("홈");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("홈");
        }
        lvUrlList.removeHeaderView(llUrlHeader);
        lvUrlList.setOnScrollListener(null);
        lvUrlList.setSelection(0);
        srlUrlList.setProgressViewOffset(true, 0, 70);
        srlUrlList.setColorSchemeResources(R.color.indigo500);
    }
    //</editor-fold>

    //<editor-fold desc="Initiate Dummy Data" defaultstate="collapsed">
    //For Test. Deprecated
    private void initUrlDummyData() {
        UrlListData urlListData = new UrlListData();
        for (int i = 0; i < 30; i++) {
            urlListData.url = "www.facebook.com";
            urlListData.urlTitle = "페북";
            urlListData.urlWriterUsrName = "ME";
            LinkBoxController.urlListSource.add(urlListData);
            urlListData = new UrlListData();
        }
    }
    private void initBoxDummyData() {
        BoxListData boxListData = new BoxListData();
        String arr[] = {"요리", "육아", "개발", "일상", "주방", "맛집", "위생", "공부"};
        for (int i = 0; i < arr.length; i++) {
            boxListData.boxName = arr[i];
            boxListData.boxKey = i;
            boxListData.boxIndex = i;
            LinkBoxController.boxListSource.add(boxListData);
            boxListData = new BoxListData();
        }
    }
    //</editor-fold>

    //<editor-fold desc="URL Inner Classes" defaultstate="collapsed">
    private class UrlLoading implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                LinkBoxController.urlListSource.clear();
                LinkBoxController.urlListSource.addAll(wrappedUrlListDatas.object);
                LinkBoxController.notifyUrlDataSetChanged();
                srlUrlList.setRefreshing(false);
                tvUrlNum.setText(Integer.toString(wrappedUrlListDatas.object.size()));
            }
            else {
                srlUrlList.setRefreshing(false);
                Toast.makeText(LinkBoxActivity.this, "URL list data가 null입니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            srlUrlList.setRefreshing(false);
            RetrofitDebug.debug(error);
            Toast.makeText(LinkBoxActivity.this, "서버와의 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class LogoutCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                Intent intent = new Intent(LinkBoxActivity.this, AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Log.d(TAG, "fail to Logout");
                Toast.makeText(LinkBoxActivity.this, "로그아웃이 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(LinkBoxActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //</editor-fold>
}
