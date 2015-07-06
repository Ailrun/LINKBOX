package org.sopt.linkbox;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by user on 2015-06-30.
 *
 */
public class LinkBoxActivity extends AppCompatActivity {

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
    private LinearLayout llBoxFooterViewAdd = null;
    private Button rlFooterButton = null;
    private LinearLayout llBoxFooterViewEdit = null;
    private EditText etAddBoxName = null;
    private Button bAddBoxCancel = null;
    private Button bToSettings = null;
    private Button bToPremium = null;

    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    //others
    private ArrayList<LinkBoxUrlListData> urlListSource = null;
    private ArrayList<LinkBoxBoxListData> boxListSource = null;

    private LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    private LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        startService(new Intent(getApplicationContext(), LinkHeadService.class));

        initData();
        initView();
        initListener();
        initControl();
    }

    private void initData() {
        immLinkBox = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
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
        ShortcutBadger.with(getApplicationContext()).count(1);
        layoutInflater = getLayoutInflater();
        //toolbar init
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setTitleTextColor(getResources().getColor(R.color.realWhite));
        tToolbar.setNavigationIcon(R.drawable.abc_ic_menu_moreoverflow_mtrl_alpha);
        if (boxListSource.size() > 0) {
            tToolbar.setTitle((boxListSource.get(0)).boxName);
        }
        else {
            tToolbar.setTitle("새 박스");
        }
        setSupportActionBar(tToolbar);
        //main init
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, null);
        lvUrlList.setEmptyView(llUrlEmptyView);
        //drawer init
        ivProfile = (ImageView) findViewById(R.id.IV_profile_link_box);
        tvBoxNumber = (TextView) findViewById(R.id.TV_box_number_link_box);
        lvBoxList = (ListView) findViewById(R.id.LV_box_list_link_box);
        llBoxFooterViewAdd = (LinearLayout) layoutInflater.inflate(R.layout.layout_footer_button_link_box, null);
        rlFooterButton = (Button) llBoxFooterViewAdd.findViewById(R.id.RL_footer_button_link_box);
        llBoxFooterViewEdit = (LinearLayout) layoutInflater.inflate(R.layout.layout_footer_edit_link_box, null);
        etAddBoxName = (EditText) llBoxFooterViewEdit.findViewById(R.id.ET_add_box_name_link_box);
        etAddBoxName.setSingleLine(true);
        etAddBoxName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        bAddBoxCancel = (Button) llBoxFooterViewEdit.findViewById(R.id.IV_add_box_cancel_link_box);
        lvBoxList.addFooterView(llBoxFooterViewAdd);
        lvBoxList.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rlFooterButton.setFocusable(true);
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
        rlFooterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoxList.removeFooterView(llBoxFooterViewAdd);
                lvBoxList.addFooterView(llBoxFooterViewEdit);
                etAddBoxName.requestFocus();
                etAddBoxName.setImeOptions(EditorInfo.IME_ACTION_NEXT);
                immLinkBox.showSoftInput(etAddBoxName, InputMethodManager.SHOW_FORCED);
            }
        });
        etAddBoxName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    immLinkBox.hideSoftInputFromWindow(etAddBoxName.getWindowToken(), 0);
                    lvBoxList.removeFooterView(llBoxFooterViewEdit);
                    lvBoxList.addFooterView(llBoxFooterViewAdd);
                    return true;
                }
                return false;
            }
        });
        bAddBoxCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lvBoxList.removeFooterView(llBoxFooterViewEdit);
                lvBoxList.addFooterView(llBoxFooterViewAdd);
            }
        });
        abBoxList = new ActionBarDrawerToggle(this, dlBoxList,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                immLinkBox.hideSoftInputFromWindow(etAddBoxName.getWindowToken(), 0);
                lvBoxList.removeFooterView(llBoxFooterViewEdit);
                lvBoxList.removeFooterView(llBoxFooterViewAdd);
                lvBoxList.addFooterView(llBoxFooterViewAdd);
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
            case R.id.action_editors :
                break;
            case R.id.action_info :
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
