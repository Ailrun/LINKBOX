package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorAdd;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorList;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.custom.adapters.imageViewAdapter.RoundedImageView;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.helper.SessionSaver;
import org.sopt.linkbox.custom.network.UrlListWrapper;
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

    private LayoutInflater layoutInflater = null;

    private UrlListWrapper urlListWrapper = null;

    //toolbar layout
    private Toolbar tToolbar = null;
    private CollapsingToolbarLayout ctlToolbar = null;
    //main layout
    private RecyclerView rvUrlList = null;
    private LinearLayout llUrlEmptyView = null;
    //drawer layout
    private RoundedImageView ivProfile = null;
    private ListView lvFavoriteBoxList = null;

    private RelativeLayout rlRecentLink = null;
    private RelativeLayout rlMyBox = null;
    private RelativeLayout rlBuyedBox = null;

    private RelativeLayout rlToSetting = null;

    private DrawerLayout dlDrawer = null;
    private ActionBarDrawerToggle abdtDrawer = null;

    private IabHelper iabHelper = null;
    private String base64EncodedPublicKey = null;
    private final String skuIDPremium = "id_mUImErpEmEkAMU";
    private List<String> skuList = null;

    // profile photo from gallery
    protected final int SELECT_GALLERY = 1;
    private Uri imgURI = null;
    private String filePath = null;
    private Bitmap bmp = null;
    private RoundedBitmapDrawable roundBitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num=" + LinkBoxController.urlListSource.size());
        initInterface();
        initData();
        initView();
        initControl();
        initListener();
    }
    @Override
    protected void onStart() {
        super.onStart();
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
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abdtDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId())
        {
            case R.id.action_share :
                startActivity(new Intent(getApplicationContext(), BoxEditorAdd.class));
                break;
            case R.id.action_editors :
                startActivity(new Intent(getApplicationContext(), BoxEditorList.class));
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {

            try {
                Log.e("DataResult", data.toString());
                imgURI = data.getData();
                // ivProfile.setImageURI(imgURI);
                // filePath = getRealPathFromURI(imgURI);
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);

                ivProfile.setImageBitmap(bmp);
                // ivProfile.getCroppedBitmap(bmp, 15);
                ivProfile.setCropToPadding(true);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void initInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    private void initData() {
        //InApp billing init
        // initInAppData();

        //other data init;
        initUrlDummyData();
        initBoxDummyData();

    }
    private  void initView() {
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
        // initInAppListener();

        //main init
        initMainListener();

        //drawer init
        initDrawerListener();
        // initDrawerButtonHeaderListener();
        // initDrawerEditHeaderListener();
    }
    private void initControl() {
        LinkBoxController.linkBoxBoxListAdapter =
            new LinkBoxBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        LinkBoxController.linkBoxUrlListAdapter =
            new LinkBoxUrlListAdapter(getApplicationContext(), LinkBoxController.urlListSource);

        rvUrlList.setAdapter(LinkBoxController.linkBoxUrlListAdapter);
        lvFavoriteBoxList.setAdapter(LinkBoxController.linkBoxBoxListAdapter);
    }

    private void initViewAfterMeasure() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        Rect rect = new Rect();
        display.getRealSize(size);
        getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        int height = size.y - rect.top - tToolbar.getHeight();
        Log.d(TAG, "height : " + size.y + "\nTool : " + tToolbar.getHeight() + ", " + tToolbar.getMeasuredHeight());
        rvUrlList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
    }

    private void initInAppData() {
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
    private void initInAppListener() {
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

    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        ctlToolbar = (CollapsingToolbarLayout) findViewById(R.id.CTL_toolbar_link_box);
        tToolbar.setNavigationIcon(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        ctlToolbar.setCollapsedTitleTextColor(getResources().getColor(R.color.real_white));
        ctlToolbar.setExpandedTitleColor(getResources().getColor(R.color.real_white));
        ctlToolbar.setTitle((LinkBoxController.boxListSource.get(LinkBoxController.currentBox.boxIndex)).boxName);
        setSupportActionBar(tToolbar);
    }

    private void initMainView() {
        rvUrlList = (RecyclerView) findViewById(R.id.RV_url_list_link_box);
//        ViewGroup viewGroup = (ViewGroup) rvUrlList.getParent();
//        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, viewGroup, false);
//        viewGroup.addView(llUrlEmptyView);
//        rvUrlList.setEmptyView(llUrlEmptyView);
    }
    private void initMainListener() {
    }

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

    }
    private void initDrawerListener() {

        rlRecentLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "");
                Intent intent = new Intent(LinkBoxActivity.this, LinkHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        rlMyBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "");
            }
        });

        rlBuyedBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG, "");
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_GALLERY);
            }
        });

        lvFavoriteBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BoxListData boxListData = (BoxListData) adapterView.getItemAtPosition(i);
                String str = null;
                if (boxListData != null) {
                    str = boxListData.boxName;
                    LinkBoxController.currentBox = (BoxListData) adapterView.getItemAtPosition(i);
                } else {
                    str = "새 박스";
                    LinkBoxController.currentBox = new BoxListData();
                }
                LinkBoxController.linkBoxUrlListAdapter.setSource(LinkBoxController.urlListSource);
                tToolbar.setTitle(str);
                dlDrawer.closeDrawers();
            }
        });
        lvFavoriteBoxList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {    // TODO : Deprecated
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
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
    }

    //For Test. Deprecated
    private void initUrlDummyData() {
        UrlListData urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
        urlListData = new UrlListData();
        urlListData.url = "www.facebook.com";
        urlListData.urlTitle = "페북";
        urlListData.urlWriterUsrName = "ME";
        LinkBoxController.urlListSource.add(urlListData);
    }
    private void initBoxDummyData() {
        BoxListData boxListData = new BoxListData();
        boxListData.boxName = "요리";
        LinkBoxController.boxListSource.add(boxListData);
        // LinkBoxController.boxEditBoxListAdapter.getView(0, boxListData, );
        boxListData = new BoxListData();
        boxListData.boxName = "육아";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "개발";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "일상";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "주방";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "맛집";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "위생";
        LinkBoxController.boxListSource.add(boxListData);
        boxListData = new BoxListData();
        boxListData.boxName = "공부";
        LinkBoxController.boxListSource.add(boxListData);
    }

    /*
    private String getRealPathFromURI(Uri uri) {
        // TODO Auto-generated method stub
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    */

    private class UrlLoading implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                LinkBoxController.urlListSource = (ArrayList<UrlListData>) wrappedUrlListDatas.object;
                LinkBoxController.notifyUrlDataSetChanged();
            }
            else {
            }
        }
        @Override
        public void failure(RetrofitError error) {
        }
    }
}
