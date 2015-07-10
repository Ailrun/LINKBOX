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

/** \���� Ȯ�� ����!
 *
 * 1. gradle ����
 * 2. AndroidManifest ����
 *  - permission �߰�
 *  - ���̽��� �߰� �±׵�
 *  - strings.xml�� App Id �߰�
 */

/**
 * https://developers.facebook.com/docs/facebook-login/android/v2.3
 */


public class FacebookLoginActivity extends Activity {

    /**
     * CallbackManager => �ݹ��� �������ִ� Ŭ�����Դϴ�.
     */
    CallbackManager callbackManager;
    LoginButton loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        /**
         * FacebookSdk.sdkInitialize(getApplicationContext())�� ��������� Facebook SDK�� �̿밡��
         * �ϴٰ� �����ֽ��ϴ�.
         */
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login);

        /**
         * callbackManager�� ��ü�� Factory�� ���ؼ� �����
         */
        callbackManager = CallbackManager.Factory.create();

        /**
         * loginButton�� �������� �ݹ��� ���(registerCallback)�մϴ�.
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
     * onResume�κп��� AccessToken�� �ִ��� �Ź� Ȯ���ϸ� �����ÿ��� �α׾ƿ��� ����̹Ƿ� �α���
     * ��ư�� �ٽ� ǥ���մϴ�.
     * AccessToken�� ���� ���� �ٷ� LoginActivity.class�� �Ѿ�ϴ�.
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
     * onActivityResult �κп��� callbackManager.onActivityResult(requestCode,resultCode,data);��
     * ����� �Ѵٰ� ���̵忡 ���õǾ� �ֽ��ϴ�.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

    }

}
