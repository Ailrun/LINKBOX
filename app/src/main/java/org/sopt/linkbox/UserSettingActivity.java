package org.sopt.linkbox;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;

import org.sopt.linkbox.custom.adapters.NotificationListAdapter;
import org.sopt.linkbox.custom.data.LinkBoxListData;

import java.util.ArrayList;


public class UserSettingActivity extends AppCompatActivity {

    Toolbar tToolbar = null;
    EditText etName = null;
    EditText etMail = null;
    EditText etChangePassword = null;
    ExpandableListView elvNotification = null;
    CheckBox cbFloating = null;
    Button bLogout = null;
    Button bSignDown = null;

    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedEditor;

    ArrayList<LinkBoxListData> groupList = null;
    ArrayList<ArrayList<LinkBoxListData>> childList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        initData();
        initView();
        initControl();
        initListener();
    }

    void initData() {
        LinkBoxListData linkBoxListData = new LinkBoxListData();
        linkBoxListData.cbname = "박스 설정";
        groupList = new ArrayList<>();
        groupList.add(linkBoxListData);
        childList = new ArrayList<>();
        childList.add(LinkBoxController.boxListSource);
    }
    void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_settings);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etName = (EditText)findViewById(R.id.ET_name_user_setting);
        etName.setTag(etName.getKeyListener());
        etName.setKeyListener(null);
        etMail = (EditText)findViewById(R.id.ET_mail_user_setting);
        etMail.setTag(etName.getKeyListener());
        etMail.setKeyListener(null);
        etChangePassword = (EditText)findViewById(R.id.ET_changepass_user_setting);
        etChangePassword.setTag(etChangePassword.getKeyListener());
        etChangePassword.setKeyListener(null);
        elvNotification = (ExpandableListView)findViewById(R.id.ELV_notification_list_user_setting);
        cbFloating = (CheckBox)findViewById(R.id.CB_floating_user_setting);
        bLogout = (Button) findViewById(R.id.B_logout_user_setting);
        bSignDown = (Button) findViewById(R.id.B_signdown_user_setting);

        // etName.setText(); 디비에서 불러와야 하니까 일단 주석
        // etMail.setText();

        sharedPref = getSharedPreferences("userProfile", 0);
        sharedEditor = sharedPref.edit();
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
                    sharedEditor.remove("usrname");

                    String name = etName.getText().toString();

                    etName.setText(name);
                    sharedEditor.putString("usrname", name);
                    sharedEditor.apply();
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
                    sharedEditor.remove("usremail");

                    String email = etMail.getText().toString();

                    etName.setText(email);
                    sharedEditor.putString("usremail", email);
                    sharedEditor.apply();
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
                    sharedEditor.remove("pass");

                    String pass = etChangePassword.getText().toString();

                    etName.setText(pass);
                    sharedEditor.putString("pass", pass);
                    sharedEditor.apply();
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
                    sharedEditor.remove("floating");

                    sharedEditor.putBoolean("floating" , b);
                    sharedEditor.apply();
                }
            }
        });

        bLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO : logout
            }
        });

        bSignDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: signdown
            }
        });


        // sharedPref - userProfile파일에 저장하고 막판에 DB 갱신?
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
