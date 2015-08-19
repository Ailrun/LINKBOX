package org.sopt.linkbox.activity.settingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

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
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
                break;

            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
@Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    void initInterface() {
        usrListWrapper = new UsrListWrapper();
    }
    void initData() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        speProfile = spProfile.edit();

        spUserSettings = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.usrListData.usrKey, 0);
        speUserSettings = spUserSettings.edit();

    }
    void initView() {
        initToolbarView();
        initMainView();
    }
    void initListener() {
        initMainListener();
    }
    void initControl() {
    }
    //</editor-fold>

    //<editor-fold desc="Initiate Toolbar" defaultstate="collapsed">
    void initToolbarView() {
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
    void initMainView() {
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
        if(LinkBoxController.defaultAlarm == true){
            cbAlarmEnable.setChecked(true);
        }
        else if(LinkBoxController.defaultAlarm == false){
            cbAlarmEnable.setChecked(false);
        }

        if(LinkBoxController.defaultReadLater == true){
            cbReadLaterEnable.setChecked(true);
        }
        else if(LinkBoxController.defaultReadLater == false){
            cbReadLaterEnable.setChecked(false);
        }
    }
    void initMainListener() {

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
                if(cbAlarmEnable.isChecked() == false){
                    LinkBoxController.defaultAlarm = true;
                    // cbAlarmEnable.setChecked(true);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("alarm_enable", 1);
                    editor.commit();

                }
                else if(cbAlarmEnable.isChecked() == true){
                    LinkBoxController.defaultAlarm = false;
                    // cbAlarmEnable.setChecked(false);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("alarm_enable", 0);
                    editor.commit();

                }
            }
        });

        cbReadLaterEnable.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(cbReadLaterEnable.isChecked() == false){
                    LinkBoxController.defaultReadLater = true;
                    // cbReadLaterEnable.setChecked(true);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("read_later_enable", 1);
                    editor.commit();
                }
                else if(cbReadLaterEnable.isChecked() == true){
                    LinkBoxController.defaultReadLater = false;
                    // cbReadLaterEnable.setChecked(false);
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("read_later_enable", 0);
                    editor.commit();
                }
            }
        });

        tvAlarmChoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingActivity.this, AlarmDialog.class);
                startActivity(intent);
            }
        });

        tvReadLaterTime.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSettingActivity.this, ReadLaterDialog.class);
                startActivity(intent);
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="User Inner Classes" defaultstate="collapsed">
    private class LogoutCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {
            Intent intent = new Intent(UserSettingActivity.this, AccountActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }
    //</editor-fold>
}
