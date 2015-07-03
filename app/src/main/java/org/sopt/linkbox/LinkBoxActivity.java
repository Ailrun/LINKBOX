package org.sopt.linkbox;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

/**
 * Created by user on 2015-06-30.
 *
 */
public class LinkBoxActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater = null;
    //toolbar layout
    private Toolbar tToolbar = null;
    //main layout
    private ListView lvUrlList = null;
    private LinearLayout llUrlEmptyView = null;
    //drawerlayout
    private ListView lvBoxList = null;
    private LinearLayout llBoxFooterViewAdd = null;
    private LinearLayout llBoxFooterViewEdit = null;
    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    private ArrayList<LinkBoxUrlListData> urlListSource = null;
    private ArrayList<LinkBoxBoxListData> boxListSource = null;

    private LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    private LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);

        initData();
        initView();
        initListener();
        initControl();
    }

    private void initData() {
        urlListSource = new ArrayList<>();
        boxListSource = new ArrayList<>();
        LinkBoxBoxListData linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "요리";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "육아";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "개발";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "일상";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "주방";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "맛집";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "위생";
        boxListSource.add(linkBoxBoxListData);
        linkBoxBoxListData = new LinkBoxBoxListData();
        linkBoxBoxListData.boxName = "공부";
        boxListSource.add(linkBoxBoxListData);
    }

    private  void initView() {
        layoutInflater = getLayoutInflater();

        //toolbar init
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.indigo500));
        if (boxListSource.size() > 0)
        {
            tToolbar.setTitle((boxListSource.get(0)).boxName);
        }
        else
        {
            tToolbar.setTitle("새 박스");
        }
        setSupportActionBar(tToolbar);

        //main init
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, null);
        lvUrlList.setEmptyView(llUrlEmptyView);
        //drawer init
        lvBoxList = (ListView) findViewById(R.id.LV_box_list_link_box);
        llBoxFooterViewAdd = (LinearLayout) layoutInflater.inflate(R.layout.layout_footer_button_link_box, null);
        lvBoxList.addFooterView(llBoxFooterViewAdd);
        dlBoxList = (DrawerLayout) findViewById(R.id.DL_root_layout);
    }

    private void initListener() {
        //main init
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
        lvBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                LinkBoxBoxListData linkBoxBoxListData = (LinkBoxBoxListData) adapterView.getItemAtPosition(i);
                String str = null;
                if (linkBoxBoxListData != null) {
                    str = linkBoxBoxListData.boxName;
                } else {
                    str = "새 박스";
                }
                tToolbar.setTitle(str);
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
