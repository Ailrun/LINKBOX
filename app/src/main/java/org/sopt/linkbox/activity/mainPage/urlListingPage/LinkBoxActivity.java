package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.h6ah4i.android.materialshadowninepatch.MaterialShadowContainerView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.helpPage.HelpActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxListEditActivity;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorList;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.constant.MainStrings;
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
    private RelativeLayout rlMyBox = null;
    private RelativeLayout rlBuyedBox = null;

    private RelativeLayout rlToSetting = null;
    private RelativeLayout rlToHelp = null;

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

    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num=" + LinkBoxController.urlListSource.size());

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dlDrawer.closeDrawers();
            }
        }, 500);




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
        switch (item.getItemId())
        {
            case R.id.action_search :
                Toast.makeText(LinkBoxActivity.this, "베타에서 만나요.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_alarms:
                Toast.makeText(LinkBoxActivity.this, "베타에서 만나요.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_editors :
                startActivity(new Intent(this, BoxEditorList.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                break;
            default :
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

        // 여기에 코드 입력

        if(LinkBoxController.inboxIndicator)
        {
            Intent intent = new Intent(LinkBoxActivity.this, BoxListEditActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            invalidateOptionsMenu();
        }
        else
        {
            Intent intent = new Intent(LinkBoxActivity.this, LinkBoxActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initPreference(){
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs != null){
            LinkBoxController.preference_readLater = prefs.getInt("read_later_preference", 2);
            int defaultAlarmIndicator = prefs.getInt("alarm_enable", 0);
            int defaultReadLaterIndicator = prefs.getInt("read_later_enable", 0);
            int new_link_alarm = prefs.getInt("new_link_alarm", 0);
            int invited_box_alarm = prefs.getInt("invited_box_alarm", 0);
            int like_alarm = prefs.getInt("like_alarm", 0);
            int comment_alarm = prefs.getInt("comment_alarm", 0);

            if(defaultAlarmIndicator == 1){
                LinkBoxController.defaultAlarm = true;
            }
            else if(defaultAlarmIndicator == 0){
                LinkBoxController.defaultAlarm = false;
            }

            if(defaultReadLaterIndicator == 1){
                LinkBoxController.defaultReadLater = true;
            }
            else if(defaultReadLaterIndicator == 0){
                LinkBoxController.defaultReadLater = false;
            }

            if(new_link_alarm == 1){
                LinkBoxController.new_link_alarm = true;
            }
            else if(new_link_alarm == 0){
                LinkBoxController.new_link_alarm = false;
            }
            if(invited_box_alarm == 1){
                LinkBoxController.invited_box_alarm = true;
            }
            else if(invited_box_alarm == 0){
                LinkBoxController.invited_box_alarm = false;
            }
            if(like_alarm == 1){
                LinkBoxController.like_alarm = true;
            }
            else if(like_alarm == 0){
                LinkBoxController.like_alarm = false;
            }
            if(comment_alarm == 1){
                LinkBoxController.comment_alarm = true;
            }
            else if(comment_alarm == 0){
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
        tToolbar.setNavigationIcon(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
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
                initInBox();
            }
        });
        lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((UrlListData) lvUrlList.getItemAtPosition(position)).url;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
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

        rlRecentLink = (RelativeLayout) findViewById(R.id.RL_recent_link_link_box);
        rlMyBox = (RelativeLayout) findViewById(R.id.RL_my_box_link_box);
        rlBuyedBox = (RelativeLayout) findViewById(R.id.RL_buyed_box);

        lvFavoriteBoxList = (ListView) findViewById(R.id.LV_favorite_box_link_box);
        lvFavoriteBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dlDrawer = (DrawerLayout) findViewById(R.id.DL_root_layout_link_box);
        rlToSetting = (RelativeLayout) findViewById(R.id.RL_setting_link_box);
        rlToHelp = (RelativeLayout) findViewById(R.id.RL_help_link_box);

        ibDeleteLinkBox = (ImageButton) findViewById(R.id.IB_delete_link_box);
        ibEditLinkBox = (ImageButton) findViewById(R.id.IB_edit_link_box);
        ibShareLinkBox = (ImageButton) findViewById(R.id.IB_share_link_box);


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

        rlMyBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                dlDrawer.closeDrawers();
                Intent intent = new Intent(LinkBoxActivity.this, BoxListEditActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            }
        });

        rlBuyedBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "BuyedBox Clicked");
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LinkBoxActivity.this, PhotoCropActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
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
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
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
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            }
        });

        rlToHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), HelpActivity.class), RESULT_HELP);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
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
            urlListWrapper.boxList(0, 20, new UrlLoading());
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
                    } else {
                        tToolbar.setTitle("");
                    }
                }
            });
            lvUrlList.setSelection(0);
            srlUrlList.setProgressViewOffset(true, 80, 150);
            srlUrlList.setColorScheme(R.color.indigo500);
        }
        else {
            Log.e(TAG, "ERROR!!! inBox=" + LinkBoxController.inboxIndicator + " and currentBox=null");
        }
    }
    private void elseInBox() {
        urlListWrapper.allList(0, 20, new UrlLoading());
        tToolbar.setTitle("최근 링크");
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("최근 링크");
        }
        lvUrlList.removeHeaderView(llUrlHeader);
        lvUrlList.setOnScrollListener(null);
        lvUrlList.setSelection(0);
        srlUrlList.setProgressViewOffset(true, 0, 70);
        srlUrlList.setColorScheme(R.color.indigo500);
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
                srlUrlList.setRefreshing(false);
                tvUrlNum.setText(Integer.toString(wrappedUrlListDatas.object.size()));
            }
            else {
                srlUrlList.setRefreshing(false);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            srlUrlList.setRefreshing(false);
            RetrofitDebug.debug(error);
        }
    }
    //</editor-fold>

}
