package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
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

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.settingPage.UserSettingActivity;
import org.sopt.linkbox.custom.adapters.imageViewAdapter.RoundedImageView;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.UrlListWrapper;
import org.sopt.linkbox.libUtils.util.IabHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LinkHomeActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LinkHomeActivity.class.getName() + " : ";

    private LayoutInflater layoutInflater = null;

    private UrlListWrapper urlListWrapper = null;

    //toolbar layout
    private Toolbar tToolbar = null;
    //main layout
    private PullToRefreshListView ptrlvUrlList = null;
    private LinearLayout linearLayout = null;
    //drawer layout

    private RoundedImageView ivProfile = null;
    private TextView tvBoxNumber = null;
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
        setContentView(R.layout.activity_link_home);
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
        getMenuInflater().inflate(R.menu.menu_link_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (abdtDrawer.onOptionsItemSelected(item)) {
            return true;
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }
    private void initView() {
        layoutInflater = getLayoutInflater();
    }
    private void initListener() {

    }
    private void initControl() {

    }

    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_home);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.real_white));
        tToolbar.setTitle("최근 링크");
        setSupportActionBar(tToolbar);
    }

    private void initMainView() {
        ptrlvUrlList = (PullToRefreshListView) findViewById(R.id.PTRLV_url_list_link_home);
    }
    private void initMainListener() {
        ptrlvUrlList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {

            }
        });
        ptrlvUrlList.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        ptrlvUrlList.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        ptrlvUrlList.getRefreshableView().setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            }
        });
    }

    private void initDrawerView() {
        ivProfile = (RoundedImageView) findViewById(R.id.RIV_profile_link_box);

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
                Log.d("", "");
            }
        });

        rlMyBox.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d("", "");
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

    private class UrlLoading implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                LinkBoxController.urlListSource = (ArrayList<UrlListData>) wrappedUrlListDatas.object;
                LinkBoxController.notifyUrlDataSetChanged();
                ptrlvUrlList.onRefreshComplete();
            }
            else {
                ptrlvUrlList.onRefreshComplete();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            ptrlvUrlList.onRefreshComplete();
        }
    }
}
