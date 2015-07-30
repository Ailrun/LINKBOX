package org.sopt.linkbox.activity.loginPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import org.sopt.linkbox.activity.loadingPage.LoginLoadingActivity;
import org.sopt.linkbox.constant.LoginStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.debugging.FacebookDebug;

import java.util.Arrays;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LoginActivity.class.getName() + " : ";

    private EditText etPass = null;
    private AutoCompleteTextView actUsremail = null;
    private Button bLogin = null;
    private LoginButton lbFacebookLogin = null;

    private SharedPreferences spProfile = null; // 02 Save preference or use preference that is saved. Bunched up data
    private SharedPreferences.Editor speProfile = null;
    private CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

        initData();

        initAutoLogin();

        initView();
        initListener();
    }
    @Override
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
    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }

    private void initData() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        speProfile = spProfile.edit();  // 03 Shared preference cannot edit data.
    }
    private void initAutoLogin() {
        String usremail = spProfile.getString(LoginStrings.usrID, "");
        String pass = spProfile.getString(LoginStrings.usrPassword, "");
        if (!usremail.equals("") && !pass.equals("")) {
            loginLoading(usremail, pass);
        }
    }
    private void initView() {
        actUsremail = (AutoCompleteTextView) findViewById(R.id.ACT_usremail_main);
        etPass = (EditText) findViewById(R.id.ET_password_main);
        bLogin = (Button) findViewById(R.id.B_login_main);
        lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_main);
    }
    private void initListener() {
        callbackManager = CallbackManager.Factory.create();
        lbFacebookLogin.setReadPermissions(Arrays.asList("email"));
        lbFacebookLogin.registerCallback(callbackManager, new FacebookLoginCallback()); // Facebook button
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usremail = actUsremail.getText().toString();
                String pass = etPass.getText().toString();
                if (usremail.equals("") || pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "All Field must be filled!", Toast.LENGTH_LONG).show();
                    return;
                }
                loginLoading(usremail, pass);
            }
        });
    }

    private void loginLoading(String usremail, String pass) {
        Intent intent = new Intent(this, LoginLoadingActivity.class);
        intent.putExtra(LoginStrings.usrID, usremail);
        intent.putExtra(LoginStrings.usrPassword, pass);
        startActivity(intent);
        finish();
    }

    //Inner Class
    private class FacebookLoginCallback implements FacebookCallback<LoginResult> {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Intent intent = new Intent(getApplicationContext(), FacebookDataActivity.class);
            startActivity(intent);
            finish();
        }
        @Override
        public void onCancel() {
            Log.d(TAG, "I cancel U!");
        }
        @Override
        public void onError(FacebookException e) {  // TODO: FACEBOOK ERROR NEEDS AN UPDATE
            FacebookDebug.debug(e);
        }
    }
}