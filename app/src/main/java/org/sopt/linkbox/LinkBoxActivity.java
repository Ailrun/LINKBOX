package org.sopt.linkbox;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

/**
 * Created by user on 2015-06-30.
 *
 */
public class LinkBoxActivity extends AppCompatActivity {

    //toolbar layout
    private Toolbar tToolbar = null;
    //main layout
    private TextView tvBoxName = null;
    private ImageButton ibEditorsInfo = null;
    private ImageButton ibInvite = null;
    private ListView lvUrlList = null;
    //drawerlayout
    private ImageButton ibToSettings = null;
    private ListView lvBoxList = null;
    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);

        initData();
        initView();
        initListener();
        initControl();
    }

    private ArrayList<LinkBoxUrlListData> urlListSource = null;
    private ArrayList<LinkBoxBoxListData> boxListSource = null;

    private void initData() {
        urlListSource = new ArrayList<>();
        boxListSource = new ArrayList<>();
        LinkBoxBoxListData linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "요리";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "육아";
        boxListSource.add(linkBoxBoxListData);
    }

    private  void initView() {
        //toolbar init
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        setSupportActionBar(tToolbar);
        tToolbar.setLogo(R.mipmap.logo);

        //main init
        tvBoxName = (TextView) findViewById(R.id.TV_box_name_link_box);
        ibEditorsInfo = (ImageButton) findViewById(R.id.IB_editors_info_link_box);
        ibInvite = (ImageButton) findViewById(R.id.IB_invite_link_box);
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
        //drawer init
        ibToSettings = (ImageButton) findViewById(R.id.IB_to_settings_link_box);
        lvBoxList = (ListView) findViewById(R.id.LV_box_list_link_box);
        dlBoxList = (DrawerLayout) findViewById(R.id.DL_root_layout);
    }

    private void initListener() {
        //main init
        ibEditorsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ibInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
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
        //drawer init
        ibToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        lvBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinkBoxBoxListData linkBoxBoxListData = (LinkBoxBoxListData) adapterView.getItemAtPosition(i);
                if (linkBoxBoxListData != null) {
                    tvBoxName.setText(linkBoxBoxListData.boxName);
                } else {
                    tvBoxName.setText("새 박스");
                }
                dlBoxList.closeDrawers();
            }
        });
        lvBoxList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                return false;
            }
        });
        abBoxList = new ActionBarDrawerToggle(this, dlBoxList,
               tToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
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
    }

    private LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    private LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;

    private void initControl() {
        linkBoxUrlListAdapter =
                new LinkBoxUrlListAdapter(getApplicationContext(), urlListSource);
        linkBoxBoxListAdapter =
                new LinkBoxBoxListAdapter(getApplicationContext(), boxListSource);
        lvUrlList.setAdapter(linkBoxUrlListAdapter);
        lvBoxList.setAdapter(linkBoxBoxListAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

// Sync the toggle state after onRestoreInstanceState has occurred.
        abBoxList.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        abBoxList.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (abBoxList.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
