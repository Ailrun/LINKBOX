package org.sopt.linkbox.activity.mainPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.libUtils.util.IabHelper;
import org.sopt.linkbox.libUtils.util.IabResult;
import org.sopt.linkbox.libUtils.util.Inventory;
import org.sopt.linkbox.service.LinkHeadService;

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

    private InputMethodManager immLinkBox = null;
    private LayoutInflater layoutInflater = null;
    //toolbar layout
    private Toolbar tToolbar = null;
    //main layout
    private ListView lvUrlList = null;
    private LinearLayout llUrlEmptyView = null;
    //drawer layout
    private ImageView ivProfile = null;
    private TextView tvBoxNumber = null;
    private ListView lvBoxList = null;
    private LinearLayout llBoxHeaderViewButton = null;
    private Button rlHeaderButton = null;
    private LinearLayout llBoxHeaderViewEdit = null;
    private EditText etAddBoxName = null;
    private Button bAddBoxCancel = null;
    private Button bToSettings = null;
    private Button bToPremium = null;

    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    private SharedPreferences sharedPreferences = null;

    private IabHelper iabHelper = null;
    private String base64EncodedPublicKey = null;
    private final String skuIDPremium = "id_mUImErpEmEkAMU";
    private List<String> skuList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        Log.d(TAG, "num="+ LinkBoxController.urlListSource.size());
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
        getMenuInflater().inflate(R.menu.menu_link_box , menu);
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
    public void onStop() {
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (iabHelper != null) {
            iabHelper.dispose();
        }
        iabHelper = null;
//        if (sharedPreferences.getBoolean("floating", true)) {
//            startService(new Intent(getApplicationContext(), LinkHeadService.class));
//        }
}

    private void initData() {
//        InApp billing init
//        initInAppData();

        //other data init;
        sharedPreferences = getSharedPreferences(SettingStrings.shared_user_settings
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
        initDrawerButtonHeaderView();
        initDrawerEditHeaderView();
    }
    private void initListener() {
        //InApp billing init
//        initInAppListener();

        //main init
        initMainListener();

        //drawer init
        initDrawerListener();
        initDrawerButtonHeaderListener();   // TODO : Delete
        initDrawerEditHeaderListener();
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
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
//        ViewGroup viewGroup = (ViewGroup) lvUrlList.getParent();
//        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, viewGroup, false);
//        viewGroup.addView(llUrlEmptyView);
//        lvUrlList.setEmptyView(llUrlEmptyView);
    }
    private void initMainListener() {
        lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });
    }

    private void initDrawerView() {
        ivProfile = (ImageView) findViewById(R.id.IV_profile_link_box);
        tvBoxNumber = (TextView) findViewById(R.id.TV_box_number_link_box);
        lvBoxList = (ListView) findViewById(R.id.LV_box_list_link_box);
        lvBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        dlBoxList = (DrawerLayout) findViewById(R.id.DL_root_layout);
        bToPremium = (Button) findViewById(R.id.B_to_premium_link_box);
        bToSettings = (Button) findViewById(R.id.B_to_settings_link_box);
    }
    private void initDrawerListener() {
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
                immLinkBox.hideSoftInputFromWindow(etAddBoxName.getWindowToken(), 0);
                lvBoxList.removeHeaderView(llBoxHeaderViewEdit);
                lvBoxList.removeHeaderView(llBoxHeaderViewButton);
                lvBoxList.addHeaderView(llBoxHeaderViewButton);
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        dlBoxList.setDrawerListener(abBoxList);
        bToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), UserSettingActivity.class));
            }
        });
        bToPremium.setOnClickListener(new View.OnClickListener() {  // TODO : Deprecated. Needs to add new buttons
            @Override
            public void onClick(View view) {
            }
        });
    }

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

}
