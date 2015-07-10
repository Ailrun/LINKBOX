package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;
import org.sopt.linkbox.service.FacebookLoginActivity;


public class LoginActivity extends AppCompatActivity {

    EditText et_pwd = null;
    AutoCompleteTextView ac_id = null;
    CheckBox chk_auto = null;


    SharedPreferences setting;
    SharedPreferences.Editor editor;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();

        Button bLogin = (Button) findViewById(R.id.B_login_main);
        LoginButton lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_main);
        ac_id = (AutoCompleteTextView) findViewById(R.id.AC_email_main);
        et_pwd = (EditText) findViewById(R.id.ET_password_main);


        /**
         * logoutButton을 가져오고 이벤트핸들러를 설정합니다.
         *
         * LoginManager.getInstance().logOut(); => LoginManager를 통해서 로그아웃을 합니다.
         */
        lbFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginButton lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_main);
                lbFacebookLogin.setBackgroundResource(R.drawable.account_facebook_login_pressed);

                LoginManager.getInstance().logOut();

                Intent intent = new Intent(getApplicationContext(), FacebookLoginActivity.class);

                startActivity(intent);
                finish();

            }
        });


        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bLogin.setBackgroundResource(R.drawable.account_login_pressed);
                String ID = ac_id.getText().toString();
                String PW = et_pwd.getText().toString();
                LinkBoxController.linkUserData.usremail = ID;
                LinkBoxController.linkUserData.pass = PW;

                if (chk_auto.isChecked()) {
                    editor.putString("usrid", ID);
                    editor.putString("pass", PW);
                    editor.putBoolean("chk_auto", true);
                    editor.commit();
                } else {
                    editor.clear();
                    editor.commit();
                }
            }
        });

        setting = getSharedPreferences("setting", 0);
        editor = setting.edit();

        if (setting.getBoolean("chk_auto", false)) {
            ac_id.setText(setting.getString("ID", ""));
            et_pwd.setText(setting.getString("PW", ""));
            chk_auto.setChecked(true);
        }
        editor.commit();

    }
}