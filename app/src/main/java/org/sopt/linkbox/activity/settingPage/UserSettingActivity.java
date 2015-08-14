package org.sopt.linkbox.activity.settingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
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

    private UsrListWrapper usrListWrapper = null;

    private Toolbar tToolbar = null;
    private TextView tvLogout = null;

    private SharedPreferences spProfile;
    private SharedPreferences.Editor speProfile;
    private SharedPreferences spUserSettings;
    private SharedPreferences.Editor speUserSettings;

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

    void initInterface() {
        usrListWrapper = new UsrListWrapper();
    }
    void initData() {
    }
    void initView() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        speProfile = spProfile.edit();

        spUserSettings = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.usrListData.usrKey, 0);
        speUserSettings = spUserSettings.edit();

        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_settings);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("설정");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        tvLogout = (TextView) findViewById(R.id.TV_logout_user_setting);
    }
    void initListener() {
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speProfile.remove(AccountStrings.usrID);
                speProfile.remove(AccountStrings.usrPassword);
                LoginManager.getInstance().logOut();
                usrListWrapper.logout(new LogoutCallback());
            }
        });
    }
    void initControl() {
    }

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
}
