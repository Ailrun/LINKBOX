package org.sopt.linkbox.service;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.sopt.linkbox.LoginActivity;
import org.sopt.linkbox.R;

/** \사전 확인 사항!
 *
 * 1. gradle 설정
 * 2. AndroidManifest 설정
 *  - permission 추가
 *  - 페이스북 추가 태그들
 *  - strings.xml에 App Id 추가
 */

/**
 * https://developers.facebook.com/docs/facebook-login/android/v2.3
 */


public class FacebookLoginActivity extends Activity {

    /**
     * CallbackManager => 콜백을 관리해주는 클래스입니다.
     */
    CallbackManager callbackManager;
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        /**
         * FacebookSdk.sdkInitialize(getApplicationContext())를 실행해줘야 Facebook SDK를 이용가능
         * 하다고 나와있습니다.
         */
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        /**
         * callbackManager의 객체를 Factory를 통해서 만들고
         */
        callbackManager = CallbackManager.Factory.create();

        /**
         * loginButton을 가져오고 콜백을 등록(registerCallback)합니다.
         */
        loginButton= (LoginButton) findViewById(R.id.LB_login_main);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onCancel() {

                Log.e("LoginActivity_onCancel", "Canceled");

            }

            @Override
            public void onError(FacebookException e) {

                Log.e("LoginActivity_onError", "Error : " + e.getMessage());

            }
        });

    }

    /**
     * onResume부분에서 AccessToken이 있는지 매번 확인하며 없을시에는 로그아웃된 경우이므로 로그인
     * 버튼을 다시 표시합니다.
     * AccessToken이 있을 때는 바로 LoginActivity.class로 넘어갑니다.
     */

    protected void onResume(){

        super.onResume();

        if(AccessToken.getCurrentAccessToken() !=null){

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

            startActivity(intent);
            finish();

        }

        else{

            loginButton.setVisibility(View.VISIBLE);

        }

    }

    /**
     * onActivityResult 부분에서 callbackManager.onActivityResult(requestCode,resultCode,data);를
     * 해줘야 한다고 가이드에 제시되어 있습니다.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

}
