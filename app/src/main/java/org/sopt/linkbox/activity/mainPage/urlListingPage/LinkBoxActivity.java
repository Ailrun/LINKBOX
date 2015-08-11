package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.helpPage.HelpActivity;
import org.sopt.linkbox.activity.mainPage.boxListPage.BoxListEditActivity;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorAdd;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorList;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.adapters.imageViewAdapter.RoundedImageView;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.helper.SessionSaver;
import org.sopt.linkbox.custom.network.MainServerWrapper;
import org.sopt.linkbox.libUtils.util.IabHelper;
import org.sopt.linkbox.libUtils.util.IabResult;
import org.sopt.linkbox.libUtils.util.Inventory;
import org.sopt.linkbox.service.pushService.LinkRegistrationService;

import java.util.ArrayList;
import java.util.List;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by Junyoung on 2015-06-30.
 *
 */

/** T?O?D?O : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
public class LinkBoxActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LinkBoxActivity.class.getName() + " : ";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private InputMethodManager immLinkBox = null;
    private LayoutInflater layoutInflater = null;

    private MainServerWrapper mainServerWrapper = null;

    //toolbar layout
    private Toolbar tToolbar = null;

    //main layout
    private PullToRefreshListView lvUrlList = null;
    private LinearLayout llUrlEmptyView = null;

    //drawer layout
    private RoundedImageView ivProfile = null;
    private TextView tvBoxNumber = null;
    private ListView lvBoxList = null;

    private RelativeLayout rlRecentLink = null;
    private RelativeLayout rlMyBox = null;
    private RelativeLayout rlBuyedBox = null;

    private PullToRefreshListView pullToRefreshView = null;
    private RelativeLayout rlToSetting = null;
    private RelativeLayout rlToHelp = null;

    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    private SharedPreferences spProfile = null;

    private IabHelper iabHelper = null;
    private String base64EncodedPublicKey = null;
    private final String skuIDPremium = "id_mUImErpEmEkAMU";
    private List<String> skuList = null;

    // profile photo from gallery
    protected final int SELECT_GALLERY = 1;
    private Uri imgURI = null;
    private String filePath = null;
    private Bitmap bmp = null;
    private RoundedBitmapDrawable roundBitmap =null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num=" + LinkBoxController.urlListSource.size());
        initInterface();
        initPush();
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
        abBoxList.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abBoxList.onConfigurationChanged(newConfig);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link_box, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abBoxList.onOptionsItemSelected(item)) {
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

    private void initInterface() {
        mainServerWrapper = new MainServerWrapper();
    }
    private void initPush() {
        if (isGoogleServiceAvailable()) {
            Intent intent = new Intent(this, LinkRegistrationService.class);
            startService(intent);
        }
    }
    private void initData() {
//        InApp billing init
//        initInAppData();

        //other data init;
        spProfile = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.userData.usrKey, 0);
        immLinkBox = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);    // TODO : This needs cleanup
        initUrlDummyData();
        initBoxDummyData();

    }
    private  void initView() {
        layoutInflater = getLayoutInflater();   // TODO : Find reference. When needs to inflate something that is not on memory

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
//        initInAppListener();

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

        lvUrlList.setAdapter(LinkBoxController.linkBoxUrlListAdapter);
        lvBoxList.setAdapter(LinkBoxController.linkBoxBoxListAdapter);
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
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
        tToolbar.setNavigationIcon(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        if (LinkBoxController.boxListSource.size() > LinkBoxController.currentBox.boxIndex) {
            tToolbar.setTitle((LinkBoxController.boxListSource.get(LinkBoxController.currentBox.boxIndex)).boxName);
        }
        else {
            tToolbar.setTitle("새 박스");
        }
        setSupportActionBar(tToolbar);
    }

    private void initMainView() {
        pullToRefreshView = (PullToRefreshListView) findViewById(R.id.LV_url_list_link_box);
        lvUrlList = (PullToRefreshListView) findViewById(R.id.LV_url_list_link_box);
//        ViewGroup viewGroup = (ViewGroup) lvUrlList.getParent();
//        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, viewGroup, false);
//        viewGroup.addView(llUrlEmptyView);
//        lvUrlList.setEmptyView(llUrlEmptyView);
    }
    private void initMainListener() {
        pullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                // Do work to refresh the list here.
            }
        });
       /* lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        lvUrlList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        lvUrlList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }
            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });*/
    }

    private void initDrawerView() {
        ivProfile = (RoundedImageView) findViewById(R.id.IV_profile_link_box);
        // tvBoxNumber = (TextView) findViewById(R.id.TV_box_number_link_box);

        rlRecentLink = (RelativeLayout) findViewById(R.id.RL_recent_link);
        rlMyBox = (RelativeLayout) findViewById(R.id.RL_my_box);
        rlBuyedBox = (RelativeLayout) findViewById(R.id.RL_buyed_box);

        lvBoxList = (ListView) findViewById(R.id.LV_favorite_box_link_box);
        lvBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dlBoxList = (DrawerLayout) findViewById(R.id.DL_root_layout);
        rlToSetting = (RelativeLayout) findViewById(R.id.RL_setting_link_box);
        rlToHelp = (RelativeLayout) findViewById(R.id.RL_help_link_box);

    }
    private void initDrawerListener() {

        rlRecentLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("", "");
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
                Log.d("", "");
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

        lvBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                dlBoxList.closeDrawers();
            }
        });
        lvBoxList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {    // TODO : Deprecated
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        abBoxList = new ActionBarDrawerToggle(this, dlBoxList,
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
        dlBoxList.setDrawerListener(abBoxList);
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
    /*
    private void initDrawerButtonHeaderView() {
        llBoxHeaderViewButton = (LinearLayout) layoutInflater.inflate(R.layout.layout_header_button_link_box, null);
        rlHeaderButton = (Button) llBoxHeaderViewButton.findViewById(R.id.RL_header_button_link_box);
        lvBoxList.addHeaderView(llBoxHeaderViewButton);
    }
    private void initDrawerEditHeaderView() {
        llBoxHeaderViewEdit = (LinearLayout) layoutInflater.inflate(R.layout.layout_header_edit_link_box, null);
        etAddBoxName = (EditText) llBoxHeaderViewEdit.findViewById(R.id.ET_add_box_name_link_box);
        etAddBoxName.setSingleLine(true);
        etAddBoxName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        bAddBoxCancel = (Button) llBoxHeaderViewEdit.findViewById(R.id.IV_add_box_cancel_link_box);
    }
    private void initDrawerButtonHeaderListener() {
        rlHeaderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoxList.removeHeaderView(llBoxHeaderViewButton);
                lvBoxList.addHeaderView(llBoxHeaderViewEdit);
                etAddBoxName.setText("");
                etAddBoxName.requestFocus();
                etAddBoxName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                immLinkBox.showSoftInput(etAddBoxName, InputMethodManager.SHOW_FORCED);
            }
        });
    }
    private void initDrawerEditHeaderListener() {
        etAddBoxName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    immLinkBox.hideSoftInputFromWindow(etAddBoxName.getWindowToken(), 0);
                    lvBoxList.removeHeaderView(llBoxHeaderViewEdit);
                    lvBoxList.addHeaderView(llBoxHeaderViewButton);
                    return true;
                }
                return false;
            }
        });
        bAddBoxCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etAddBoxName.setText("");
                immLinkBox.hideSoftInputFromWindow(etAddBoxName.getWindowToken(), 0);
                lvBoxList.removeHeaderView(llBoxHeaderViewEdit);
                lvBoxList.addHeaderView(llBoxHeaderViewButton);
            }
        });
    }
    */
    private boolean isGoogleServiceAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "This device does not support Google Play Service :(", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    private void setIconBadge(int i) {  // TODO : Amount of push alarm. Ex) facebook alarm
        ShortcutBadger.with(getApplicationContext()).count(1);
    }


    //For Test. Deprecated
    private void initUrlDummyData() {
        UrlListData urlListData = new UrlListData();
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
            } catch (Exception e) {
            }
        }
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
}
