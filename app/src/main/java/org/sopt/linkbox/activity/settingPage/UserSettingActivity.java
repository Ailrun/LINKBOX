package org.sopt.linkbox.activity.settingPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loginPage.AccountActivity;
import org.sopt.linkbox.constant.LoginStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.adapters.listViewAdapter.NotificationListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;

import java.util.ArrayList;


public class UserSettingActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private EditText etName = null;
    private EditText etMail = null;
    private EditText etChangePassword = null;
    private ExpandableListView elvNotification = null;
    private CheckBox cbFloating = null;
    private Button bLogout = null;
    private Button bSignDown = null;

    private SharedPreferences sharedPreferencesForProfile;
    private SharedPreferences.Editor sharedEditorForProfile;
    private SharedPreferences sharedPreferencesForSetting;
    private SharedPreferences.Editor sharedEditorForSetting;

    private ArrayList<BoxListData> groupList = null;
    private ArrayList<ArrayList<BoxListData>> childList = null;

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
        BoxListData boxListData = new BoxListData();
        boxListData.boxName = "박스 설정";
        groupList = new ArrayList<>();
        groupList.add(boxListData);
        childList = new ArrayList<>();
        childList.add(LinkBoxController.boxListSource);
    }
    void initView() {
        sharedPreferencesForProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        sharedEditorForProfile = sharedPreferencesForProfile.edit();

        sharedPreferencesForSetting = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.userData.usrKey, 0);
        sharedEditorForSetting = sharedPreferencesForSetting.edit();

        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_settings);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName = (EditText)findViewById(R.id.ET_name_user_setting);
        etName.setTag(etName.getKeyListener());
        etName.setKeyListener(null);
        etName.setText(LinkBoxController.userData.usrName);
        etMail = (EditText)findViewById(R.id.ET_mail_user_setting);
        etMail.setTag(etName.getKeyListener());
        etMail.setKeyListener(null);
        etMail.setText(LinkBoxController.userData.usrID);
        etChangePassword = (EditText)findViewById(R.id.ET_changepass_user_setting);
        etChangePassword.setTag(etChangePassword.getKeyListener());
        etChangePassword.setKeyListener(null);
        etChangePassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etChangePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        elvNotification = (ExpandableListView)findViewById(R.id.ELV_notification_list_user_setting);
        cbFloating = (CheckBox)findViewById(R.id.CB_floating_user_setting);
        cbFloating.setChecked(sharedPreferencesForSetting.getBoolean("floating", true));
        bLogout = (Button) findViewById(R.id.B_logout_user_setting);
        bSignDown = (Button) findViewById(R.id.B_signdown_user_setting);
    }
    void initListener() {

        /************* 사용자 이름, 메일 수정 리스너 *************/
        etName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                etName.setKeyListener((KeyListener) etName.getTag());
                return false;
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    etName.setKeyListener(null);
                }
            }
        });
        etName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    // 이전 설정 이름 삭제
                    sharedEditorForSetting.remove("usrName");

                    String name = etName.getText().toString();

                    etName.setText(name);
                    sharedEditorForSetting.putString("usrName", name);
                    sharedEditorForSetting.apply();
                }
                return false;
            }
        });
        etMail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                etMail.setKeyListener((KeyListener)etMail.getTag());
                return false;
            }
        });
        etMail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    etMail.setKeyListener(null);
                }
            }
        });
        etMail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    sharedEditorForSetting.remove("usrID");

                    String email = etMail.getText().toString();

                    etName.setText(email);
                    sharedEditorForSetting.putString("usrID", email);
                    sharedEditorForSetting.apply();
                }
                return false;
            }
        });

        etChangePassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                etChangePassword.setKeyListener((KeyListener)etChangePassword.getTag());
                return false;
            }
        });
        etChangePassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    etChangePassword.setKeyListener(null);
                }
            }
        });
        etChangePassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    sharedEditorForSetting.remove("usrPassword");

                    String pass = etChangePassword.getText().toString();

                    etName.setText(pass);
                    sharedEditorForSetting.putString("usrPassword", pass);
                    sharedEditorForSetting.apply();
                }
                return false;
            }
        });

        findViewById(R.id.IB_expand_user_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "TEST/nofi : " + "out class click");
                if (elvNotification.isGroupExpanded(0)) {
                    elvNotification.collapseGroup(0);
                } else {
                    elvNotification.expandGroup(0);
                }
            }
        });

        cbFloating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    sharedEditorForSetting.remove("floating");

                    sharedEditorForSetting.putBoolean("floating", b);
                    sharedEditorForSetting.apply();
                }
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedEditorForProfile.remove(LoginStrings.usrID);
                sharedEditorForProfile.remove(LoginStrings.usrPassword);
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
        LinkBoxController.notificationListAdapter = new NotificationListAdapter(getApplicationContext(), groupList, childList);
        elvNotification.setAdapter(LinkBoxController.notificationListAdapter);
        elvNotification.expandGroup(0);
    }
}
