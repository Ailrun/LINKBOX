package org.sopt.linkbox.activity.settingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.accountPage.AccountActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.usr.UsrListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class UserSettingActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + UserSettingActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private UsrListWrapper usrListWrapper = null;

    private Toolbar tToolbar = null;

    private TextView tvUsrName = null;
    private TextView tvUsrID = null;
    private TextView tvLogout = null;

    private CheckBox cbAlarmEnable = null;
    private CheckBox cbReadLaterEnable = null;
    private TextView tvAlarmChoice = null;
    private TextView tvReadLaterTime = null;

    private SharedPreferences spProfile;
    private SharedPreferences.Editor speProfile;
    private SharedPreferences spUserSettings;
    private SharedPreferences.Editor speUserSettings;

    private SharedPreferences spAlarm;
    private SharedPreferences spReadLater;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user_setting);


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
    @Override
    protected void onStop() {
        super.onStop();
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
        usrListWrapper = new UsrListWrapper();
    }
    private void initData() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        speProfile = spProfile.edit();

        spUserSettings = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.usrListData.usrKey, 0);
        speUserSettings = spUserSettings.edit();

    }
    private void initView() {
        initToolbarView();
        initMainView();
    }
    private void initListener() {
        initMainListener();
    }
    private  void initControl() {
    }
    //</editor-fold>

    //<editor-fold desc="Initiate Toolbar" defaultstate="collapsed">
    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_settings);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("설정");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Main" defaultstate="collapsed">
    private void initMainView() {
        tvUsrName = (TextView) findViewById(R.id.TV_user_name_user_setting);
        tvLogout = (TextView) findViewById(R.id.TV_logout_user_setting);
        tvUsrID = (TextView) findViewById(R.id.TV_user_id_user_setting);

        cbAlarmEnable = (CheckBox) findViewById(R.id.CB_alarm_enable);
        cbReadLaterEnable = (CheckBox) findViewById(R.id.CB_read_later_enable);

        tvAlarmChoice = (TextView) findViewById(R.id.TV_alarm_choice);
        tvReadLaterTime = (TextView) findViewById(R.id.TV_read_later_time);

        tvUsrName.setText(LinkBoxController.usrListData.usrName);
        tvUsrID.setText(LinkBoxController.usrListData.usrID);

        // Set checked for CheckBoxes
        if(LinkBoxController.defaultAlarm){
            cbAlarmEnable.setChecked(true);
        }
        else {
            cbAlarmEnable.setChecked(false);
        }

        if(LinkBoxController.defaultReadLater){
            cbReadLaterEnable.setChecked(true);
        }
        else {
            cbReadLaterEnable.setChecked(false);
        }
    }
    private void initMainListener() {

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speProfile.remove(AccountStrings.usrID);
                speProfile.remove(AccountStrings.usrPassword);
                LoginManager.getInstance().logOut();
                usrListWrapper.logout(new LogoutCallback());
            }
        });

        cbAlarmEnable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                if(cbAlarmEnable.isChecked()){
                    LinkBoxController.defaultAlarm = false;
                    // cbAlarmEnable.setChecked(false);
                    editor.putInt("alarm_enable", 0);
                }
                else {
                    LinkBoxController.defaultAlarm = true;
                    // cbAlarmEnable.setChecked(true);
                    editor.putInt("alarm_enable", 1);
                }
                editor.apply();
            }
        });

        cbReadLaterEnable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(cbReadLaterEnable.isChecked()){
                    LinkBoxController.defaultReadLater = false;
                    // cbReadLaterEnable.setChecked(false);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("read_later_enable", 0);
                    editor.commit();
                }
                else {
                    LinkBoxController.defaultReadLater = true;
                    // cbReadLaterEnable.setChecked(true);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("read_later_enable", 1);
                    editor.commit();
                }
            }
        });

        tvAlarmChoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingActivity.this, AlarmDialogActivity.class);
                startActivity(intent);
            }
        });

        tvReadLaterTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingActivity.this, ReadLaterDialogActivity.class);
                startActivity(intent);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="User Inner Classes" defaultstate="collapsed">
    private class LogoutCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                Intent intent = new Intent(UserSettingActivity.this, AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                LinkBoxController.usrListData = null;
                finish();
            } else {
                Log.d(TAG, "fail to Logout");
                Toast.makeText(UserSettingActivity.this, "로그아웃이 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(UserSettingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //</editor-fold>
}
