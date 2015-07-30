package org.sopt.linkbox.activity.settingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loginPage.AccountActivity;
import org.sopt.linkbox.constant.LoginStrings;
import org.sopt.linkbox.constant.SettingStrings;


public class UserSettingActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private Button bLogout = null;
    private Button bSignDown = null;

    private SharedPreferences spProfile;
    private SharedPreferences.Editor speProfile;
    private SharedPreferences spUserSettings;
    private SharedPreferences.Editor speUserSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_user_setting);

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

    void initData() {
    }
    void initView() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        speProfile = spProfile.edit();

        spUserSettings = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.userData.usrKey, 0);
        speUserSettings = spUserSettings.edit();

        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_settings);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bLogout = (Button) findViewById(R.id.B_logout_user_setting);
    }
    void initListener() {
        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speProfile.remove(LoginStrings.usrID);
                speProfile.remove(LoginStrings.usrPassword);
                Intent intent = new Intent(UserSettingActivity.this, AccountActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                LoginManager.getInstance().logOut();
                startActivity(intent);
                finish();
            }
        });

        bSignDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: signdown
            }
        });


        // sharedPreferences - userProfile파일에 저장하고 막판에 DB 갱신?
        /******************************************************/

        // 로그아웃 버튼
        // 회원탈퇴 버튼
    }
    void initControl() {
    }
}
