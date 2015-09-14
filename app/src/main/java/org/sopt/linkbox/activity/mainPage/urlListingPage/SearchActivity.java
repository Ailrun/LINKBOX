package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.boxListPage.WebviewActivity;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkBoxBoxListAdapter;
import org.sopt.linkbox.custom.adapters.swapeListViewAdapter.LinkBoxUrlListAdapter;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.custom.widget.SoundSearcher;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SearchActivity extends AppCompatActivity {

    private LayoutInflater layoutInflater = null;

    private Toolbar tToolbar = null;
    private SearchView svSearchView = null;
    private MenuItem menuItems = null;

    private ListView lvUrlList = null;
    private TextView tvUrlNum = null;

    private UrlListData urlListData = null;
    private UrlListWrapper urlListWrapper = null;

    private int iUrlNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initInterface();
        initToolbarView();
        initView();
        initControl();
        initListener();

        urlListWrapper.allList(0, 50, new UrlLoading());
        lvUrlList.setOnScrollListener(null);
        lvUrlList.setSelection(0);
    }

    private void initListener(){
        lvUrlList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, WebviewActivity.class);

                intent.putExtra(MainStrings.position, position);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            }
        });
    }

    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_search);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tToolbar.setTitleTextColor(Color.WHITE);


    }

    private void initView(){
        layoutInflater = getLayoutInflater();

        lvUrlList = (ListView)findViewById(R.id.LV_url_list_search);
        tvUrlNum = (TextView)findViewById(R.id.TV_Url_Number_search);

    }
    private void initInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    private void initControl() {
        //TODO : Change To FavoriteBox's Adapter
        LinkBoxController.linkBoxUrlListAdapter =
                new LinkBoxUrlListAdapter(getApplicationContext(), LinkBoxController.urlListSource);//이게 맞나??

        lvUrlList.setTextFilterEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        menu = tToolbar.getMenu();
        menuItems = menu.add("search");
        menuItems.setIcon(R.drawable.ic_search_white_24px);
        menuItems.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuItems.setActionView(svSearchView);



        svSearchView = (SearchView)tToolbar.getMenu().findItem(R.id.action_search).getActionView();
        svSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                    initControl();
                    LinkBoxController.linkBoxUrlListAdapter.filter(newText);
                    lvUrlList.setAdapter(LinkBoxController.linkBoxUrlListAdapter);
                    tvUrlNum.setText("검색결과 : "+lvUrlList.getAdapter().getCount() + "개");

                return false;
            }
        });
        svSearchView.setIconified(false);
        svSearchView.setIconifiedByDefault(false);
        svSearchView.clearFocus();
        svSearchView.setQueryHint("검색할 문자");
        return true;
    }

    private class UrlLoading implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                LinkBoxController.urlListSource.clear();
                LinkBoxController.urlListSource.addAll(wrappedUrlListDatas.object);
                LinkBoxController.notifyUrlDataSetChanged();

            }
            else {
                Toast.makeText(SearchActivity.this, "URL list data가 null입니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(SearchActivity.this, "서버와의 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }
}
