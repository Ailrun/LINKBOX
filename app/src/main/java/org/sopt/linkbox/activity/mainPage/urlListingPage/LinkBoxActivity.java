package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.helpPage.HelpActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxListEditActivity;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorList;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.custom.adapters.imageViewAdapter.RoundedImageView;
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

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private LayoutInflater layoutInflater = null;

    private UrlListWrapper urlListWrapper = null;

    private boolean inBox = false;
    private String boxTitle = null;

    //toolbar layout
    private Toolbar tToolbar = null;
    private Menu menu = null;
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

    // profile photo from gallery
    private Uri imgURI = null;
    private String filePath = null;
    private Bitmap bmp = null;
    private Bitmap user_image = null;
    private RoundedBitmapDrawable roundBitmap = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num=" + LinkBoxController.urlListSource.size());

        user_image = ImageSaveLoad.loadProfileImage();
        LinkBoxController.userImage = user_image;

        initInterface();
        initData();
        initView();
        initControl();
        initListener();
        initInBox();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (LinkBoxController.userImage != null) {
            ivProfile.setImageBitmap(LinkBoxController.userImage);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
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
        this.menu = menu;
        menuItems = new MenuItem[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            menuItems[i] = menu.getItem(i);
        }
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItems[1].setVisible(!inBox);
        menuItems[2].setVisible(inBox);
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
                break;
            case R.id.action_alarms:
                break;
            case R.id.action_editors :
                startActivity(new Intent(this, BoxEditorList.class));
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
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    private void initData() {
        //InApp billing init
        // initInAppPayData();

        //Page Data
        inBox = getIntent().getBooleanExtra("inBox", false);

        //other data init;
        initUrlDummyData();
        initBoxDummyData();

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
                urlListWrapper.boxList(0, 20, new UrlLoading());
            }
        });
        lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = ((UrlListData)lvUrlList.getItemAtPosition(position)).url;
            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Drawers" defaultstate="collapsed">
    private void initDrawerView() {
        ivProfile = (RoundedImageView) findViewById(R.id.RIV_profile_link_box);
        // tvBoxNumber = (TextView) findViewById(R.id.TV_box_number_link_box);

        rlRecentLink = (RelativeLayout) findViewById(R.id.RL_recent_link_link_box);
        rlMyBox = (RelativeLayout) findViewById(R.id.RL_my_box_link_box);
        rlBuyedBox = (RelativeLayout) findViewById(R.id.RL_buyed_box);

        lvFavoriteBoxList = (ListView) findViewById(R.id.LV_favorite_box_link_box);
        lvFavoriteBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dlDrawer = (DrawerLayout) findViewById(R.id.DL_root_layout_link_box);
        rlToSetting = (RelativeLayout) findViewById(R.id.RL_setting_link_box);
        rlToHelp = (RelativeLayout) findViewById(R.id.RL_help_link_box);
    }
    private void initDrawerListener() {

        rlRecentLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                inBox = false;
                urlListWrapper.allList(0, 20, new UrlLoading());
                initInBox();
                invalidateOptionsMenu();
                dlDrawer.closeDrawers();
            }
        });

        rlMyBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(LinkBoxActivity.this, BoxListEditActivity.class);
                startActivity(intent);
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
            }
        });

        lvFavoriteBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinkBoxController.currentBox = (BoxListData) adapterView.getItemAtPosition(i);
                inBox = true;
                urlListWrapper.boxList(0, 20, new UrlLoading());
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

        rlToHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), HelpActivity.class));
            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate InBox" defaultstate="collapsed">
    private void initInBox() {
        if (inBox) {
            if (LinkBoxController.currentBox != null) {
                boxTitle = LinkBoxController.currentBox.boxName;
                tToolbar.setTitle("");
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
                Log.e(TAG, "ERROR!!! inBox=" + inBox + " and currentBox=null");
            }
        } else {
            tToolbar.setTitle("최근 링크");
            lvUrlList.removeHeaderView(llUrlHeader);
            lvUrlList.setOnScrollListener(null);
            lvUrlList.setSelection(0);
            srlUrlList.setProgressViewOffset(true, 0, 70);
            srlUrlList.setColorScheme(R.color.indigo500);
        }
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
                LinkBoxController.urlListSource = (ArrayList<UrlListData>) wrappedUrlListDatas.object;
                LinkBoxController.notifyUrlDataSetChanged();
                srlUrlList.setRefreshing(false);
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
