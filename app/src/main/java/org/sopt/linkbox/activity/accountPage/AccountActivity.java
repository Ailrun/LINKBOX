package org.sopt.linkbox.activity.accountPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.helper.SessionSaver;
import org.sopt.linkbox.debugging.FacebookDebug;

import java.util.Collections;


public class AccountActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + AccountActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private LoginButton lbFacebookLogin = null;
    private Button bFacebookLogin = null;
    private Button bGoogleLogin = null;
    private Button bLogin = null;
    private Button bSignup = null;

    private SharedPreferences spProfile = null; // 02 Save preference or use preference that is saved. Bunched up data

    private CallbackManager callbackManager = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_account);

        initData();
        initAutoLogin();
        initView();
        initTouchListener();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AccessToken.getCurrentAccessToken() != null) {
            startActivity(new Intent(getApplicationContext(), FacebookDataActivity.class));
            finish();
        } else {
            lbFacebookLogin.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SessionSaver.saveSession(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initData() {
        spProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
    }

    private void initAutoLogin() {
        String usremail = spProfile.getString(AccountStrings.usrID, "");
        String pass = spProfile.getString(AccountStrings.usrPassword, "");
        if (!usremail.equals("") && !pass.equals("")) {
            loginLoading(usremail, pass);
        }
    }

    private void initView() {
        lbFacebookLogin = (LoginButton) findViewById(R.id.LB_login_account);
        bFacebookLogin = (Button) findViewById(R.id.B_facebook_login_account);
        bGoogleLogin = (Button) findViewById(R.id.B_google_login_account);
        bLogin = (Button) findViewById(R.id.B_login_account);
        bSignup = (Button) findViewById(R.id.B_signup_account);
    }

    private void initTouchListener() {
        bLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bLogin.setBackgroundResource(R.drawable.custom_border_inverted);
                    bLogin.setTextColor(getResources().getColor(R.color.indigo500));
                } else {
                    bLogin.setBackgroundResource(R.drawable.custom_border);
                    bLogin.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });
        bSignup.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bSignup.setBackgroundResource(R.drawable.custom_border_inverted);
                    bSignup.setTextColor(getResources().getColor(R.color.indigo500));
                } else {
                    bSignup.setBackgroundResource(R.drawable.custom_border);
                    bSignup.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });

        bFacebookLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bFacebookLogin.setBackgroundResource(R.drawable.custom_border_inverted);
                    bFacebookLogin.setTextColor(getResources().getColor(R.color.indigo500));
                } else {
                    bFacebookLogin.setBackgroundResource(R.drawable.custom_border);
                    bFacebookLogin.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });
        bGoogleLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bGoogleLogin.setBackgroundResource(R.drawable.custom_border_inverted);
                    bGoogleLogin.setTextColor(getResources().getColor(R.color.indigo500));
                } else {
                    bGoogleLogin.setBackgroundResource(R.drawable.custom_border);
                    bGoogleLogin.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });
    }

    private void initListener() {
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccountActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
        bFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lbFacebookLogin.performClick();
            }
        });
        bGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "베타에서 만나요!", Toast.LENGTH_LONG).show();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        lbFacebookLogin.setReadPermissions(Collections.singletonList("email"));
        lbFacebookLogin.registerCallback(callbackManager, new FacebookLoginCallback()); // Facebook button
    }
    //</editor-fold>

    //<editor-fold desc="Account Inner Classes" defaultstate="collapsed">
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
            Toast.makeText(AccountActivity.this, "페이스북 로그인에 문제가 생겼습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    //</editor-fold>
    //<editor-fold desc="Account Helper Methods" defaultstate="collapsed">
    private void loginLoading(String usremail, String pass) {
        Intent intent = new Intent(this, AccountLoadingActivity.class);
        intent.putExtra(AccountStrings.usrID, usremail);
        intent.putExtra(AccountStrings.usrPassword, pass);
        startActivity(intent);
    }
    //</editor-fold>
}
