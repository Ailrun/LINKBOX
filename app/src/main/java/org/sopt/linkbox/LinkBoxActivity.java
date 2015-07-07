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
import android.view.ViewGroup;
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

import org.sopt.linkbox.custom.adapters.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.LinkBoxBoxListData;
import org.sopt.linkbox.custom.data.LinkBoxUrlListData;
import org.sopt.linkbox.service.LinkHeadService;

import java.util.ArrayList;

import me.leolin.shortcutbadger.ShortcutBadger;

import static org.sopt.linkbox.debugging.TaskDebugging.debug;

/**
 * Created by Junyoung on 2015-06-30.
 *
 */

/** TODO : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
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
    private LinearLayout llBoxHeaderViewButton = null;
    private Button rlHeaderButton = null;
    private LinearLayout llBoxHeaderViewEdit = null;
    private EditText etAddBoxName = null;
    private Button bAddBoxCancel = null;
    private Button bToSettings = null;
    private Button bToPremium = null;

    private DrawerLayout dlBoxList = null;
    private ActionBarDrawerToggle abBoxList = null;

    //others
    private ArrayList<LinkBoxUrlListData> urlListSource = null;
    ArrayList<LinkBoxBoxListData> boxListSource = null;

    private LinkBoxUrlListAdapter linkBoxUrlListAdapter = null;
    LinkBoxBoxListAdapter linkBoxBoxListAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box);
        startService(new Intent(getApplicationContext(), LinkHeadService.class));
//      For Debug : Start
        debug(this);
        if (!getIntent().hasExtra("aa")) {
            Intent intent = new Intent(getApplicationContext(), LinkBoxActivity.class);
            intent.putExtra("aa", 5);
            startActivity(intent);
        }
//      For Debug : End
        initData();
        initView();
        initListener();
        initControl();
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
        layoutInflater = getLayoutInflater();

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
        //main init
        initMainListener();

        //drawer init
        initDrawerListener();
        initDrawerButtonHeaderListener();
        initDrawerEditHeaderListener();
    }
    private void initControl() {
        linkBoxUrlListAdapter =
                new LinkBoxUrlListAdapter(getApplicationContext(), urlListSource);
        linkBoxBoxListAdapter =
                new LinkBoxBoxListAdapter(getApplicationContext(), boxListSource);
        lvUrlList.setAdapter(linkBoxUrlListAdapter);
        lvBoxList.setAdapter(linkBoxBoxListAdapter);
    }

    private void initToolbarView() {
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
    }

    private void initMainView() {
        lvUrlList = (ListView) findViewById(R.id.LV_url_list_link_box);
        ViewGroup viewGroup = (ViewGroup) lvUrlList.getParent();
        llUrlEmptyView = (LinearLayout) layoutInflater.inflate(R.layout.layout_url_list_empty_link_box, viewGroup, false);
        viewGroup.addView(llUrlEmptyView);
        lvUrlList.setEmptyView(llUrlEmptyView);
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
    }
    private void initDrawerListener() {
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

    private void setIconBadge(int i) {
        ShortcutBadger.with(getApplicationContext()).count(1);
    }
}
