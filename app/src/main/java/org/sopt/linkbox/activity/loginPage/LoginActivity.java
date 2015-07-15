package org.sopt.linkbox.activity.loginPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.UserData;
import org.sopt.linkbox.custom.network.MainServerWrapper;
import org.sopt.linkbox.debugging.FacebookDebug;
import org.sopt.linkbox.debugging.RetrofitDebug;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginActivity extends AppCompatActivity {

    EditText et_pass = null;
    AutoCompleteTextView ac_email = null;
    Button bLogin = null;
    LoginButton lbFacebookLogin = null;


    SharedPreferences setting;
    SharedPreferences.Editor editor;
    CallbackManager callbackManager;
    MainServerWrapper mainServerWrapper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        initInterface();
        initData();

        initAutoLogin();

        initView();
        initListener();
    }
    protected void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken() !=null){
            startActivity(new Intent(getApplicationContext(), FacebookDataActivity.class));
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

    private void initInterface() {
        mainServerWrapper = new MainServerWrapper();
    }
    private void initData() {
        setting = getSharedPreferences(getString(R.string.shared_user_profiles), 0);
        editor = setting.edit();
    }
    private void initAutoLogin() {
        if (!setting.getString("usremail", "").equals("") && !setting.getString("pass", "").equals("")) {
            String usremail = setting.getString("usremail", "");
            String pass = setting.getString("pass", "");
            mainServerWrapper.postLoginAsync(usremail, pass, new LoginCallback());
        }
    }
    private void initView() {
        ac_email = (AutoCompleteTextView) findViewById(R.id.AC_email_main);
        et_pass = (EditText) findViewById(R.id.ET_password_main);
        bLogin = (Button) findViewById(R.id.B_login_main);
        lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_main);
    }
    private void initListener() {
        callbackManager = CallbackManager.Factory.create();
        lbFacebookLogin.registerCallback(callbackManager, new FacebookLoginCallback());
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = ac_email.getText().toString();
                String pass = et_pass.getText().toString();
                if (email.equals("") || pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "All Field must be filled!", Toast.LENGTH_LONG).show();
                    return;
                }
                mainServerWrapper.postLoginAsync(email, pass, new LoginCallback());
            }
        });
    }

    private class LoginCallback implements Callback<UserData> {
        @Override
        public void success(UserData userData, Response response) {

        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            startActivity(new Intent(getApplicationContext(), FacebookDataActivity.class));
            finish();
        }
        @Override
        public void onCancel() {
        }
        @Override
        public void onError(FacebookException e) {
            FacebookDebug.debug(e);
        }
    }
}