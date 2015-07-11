package org.sopt.linkbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.sopt.linkbox.custom.network.LinkNetwork;
import org.sopt.linkbox.service.LinkHeadService;


public class LoginActivity extends AppCompatActivity {

    EditText et_pwd = null;
    AutoCompleteTextView ac_id = null;
    Button bLogin = null;
    LoginButton lbFacebookLogin = null;


    SharedPreferences setting;
    SharedPreferences.Editor editor;
    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        setting = getSharedPreferences("user", 0);
        editor = setting.edit();

        if (!setting.getString("usremail", "").equals("") && !setting.getString("pass", "").equals("")) {
            LinkBoxController.linkUserData.usremail = setting.getString("usremail", "");
            LinkBoxController.linkUserData.pass = setting.getString("pass", "");
            LinkNetwork.Server.postLoginToServerAsync(this);
        }
        callbackManager = CallbackManager.Factory.create();

        ac_id = (AutoCompleteTextView) findViewById(R.id.AC_email_main);
        et_pwd = (EditText) findViewById(R.id.ET_password_main);
        bLogin = (Button) findViewById(R.id.B_login_main);
        lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_main);

        /**
        /**
         * logoutButton을 가져오고 이벤트핸들러를 설정합니다.
         *
         * LoginManager.getInstance().logOut(); => LoginManager를 통해서 로그아웃을 합니다.
         */
        lbFacebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                startActivity(new Intent(getApplicationContext(), LoginDataActivity.class));
                finish();
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException e) {
            }
        });
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = ac_id.getText().toString();
                String PW = et_pwd.getText().toString();
                if (ID.equals("") || PW.equals("")) {
                    Toast.makeText(getApplicationContext(), "All Field must be filled!", Toast.LENGTH_LONG).show();
                }

                LinkBoxController.linkUserData.usremail = ID;
                LinkBoxController.linkUserData.pass = PW;

                editor.putString("usremail", ID);
                editor.putString("pass", PW);
                editor.apply();

                LinkNetwork.Server.postLoginToServerAsync(LoginActivity.this);
            }
        });
    }
    protected void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken() !=null){
            startActivity(new Intent(getApplicationContext(), LoginDataActivity.class));
            finish();
        }
        else{
            lbFacebookLogin.setVisibility(View.VISIBLE);
        }
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }
}