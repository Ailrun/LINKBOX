package org.sopt.linkbox.activity.alarmPage;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.boxListPage.WebviewActivity;
import org.sopt.linkbox.constant.AlarmType;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.adapters.listViewAdapter.AlarmListAdapter;
import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.alarm.AlarmListWrapper;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AlarmActivity extends AppCompatActivity {

    private static final String TAG = "TEST/" + AlarmActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private AlarmListWrapper alarmListWrapper = null;
    private AlarmListData alarmListData = null;

    private Toolbar tToolbar = null;

    private ListView lvAlarmList = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        initInterface();
        initData();

        initView();

        initControl();

        initListener();
    }
    @Override
    public void onResume() {
        super.onResume();
    }


    public void initListener(){
        lvAlarmList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alarmListData = new AlarmListData();
                alarmListData = (AlarmListData) parent.getItemAtPosition(position);
                alarmListData.alarmRead = 1;
                switch (alarmListData.alarmType)
                {
                    case AlarmType.typeUrl:
                        view.setBackgroundResource(R.color.real_white);
                        LinkBoxController.alarmListAdapter.notifyDataSetChanged();
                        alarmListWrapper.read(alarmListData, new ReadCallback());

                        break;

                    case AlarmType.typeGood:
                        view.setBackgroundResource(R.color.real_white);
                        LinkBoxController.alarmListAdapter.notifyDataSetChanged();
                        alarmListWrapper.read(alarmListData, new ReadCallback());

                        break;
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();

                break;

            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();

    }
    //</editor-fold>



    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initInterface() {
        alarmListWrapper = new AlarmListWrapper();
    }
    private void initData() {
        alarmListWrapper.allList(new AlarmListCallback());
    }
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_alarm_list);  // TODO : REVIVE THIS PART AFTER FINISHING THE REST
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("알림페이지");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        lvAlarmList = (ListView) findViewById(R.id.LV_alarm_list);
        // Code for changing the toolbar backbutton color
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.real_white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

    }
    private void initControl() {

        LinkBoxController.alarmListAdapter =
                new AlarmListAdapter(getApplicationContext(), LinkBoxController.alarmBoxListSource);
        if (LinkBoxController.alarmListAdapter != null) {
            lvAlarmList.setAdapter(LinkBoxController.alarmListAdapter);
        }
    }
    //</editor-fold>


    private class AlarmListCallback implements Callback<MainServerData<List<AlarmListData>>> {
        @Override
        public void success(MainServerData<List<AlarmListData>> wrappedAlarmListDatas, Response response) {
            if (wrappedAlarmListDatas.result) {
                LinkBoxController.alarmBoxListSource.clear();
                LinkBoxController.alarmBoxListSource.addAll(wrappedAlarmListDatas.object);
                LinkBoxController.notifyAlarmDataSetChanged();
            }
            else {
                Toast.makeText(AlarmActivity.this, "초대 목록 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(AlarmActivity.this, "서버와 연결할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }


    private class ReadCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                Intent intent = new Intent(AlarmActivity.this, WebviewActivity.class);
                int  key = LinkBoxController.linkBoxUrlListAdapter.getItemPostionAsKey(alarmListData.alarmUrlKey);
                if(key <0)
                {
                    Toast.makeText(AlarmActivity.this, "url키가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.putExtra(MainStrings.position, key);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(intent);
            }
            else {
                Log.d(TAG, "Fail to go to url");
                Toast.makeText(AlarmActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Log.d(TAG, "Fail to decline at all");
            Toast.makeText(AlarmActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }

}
