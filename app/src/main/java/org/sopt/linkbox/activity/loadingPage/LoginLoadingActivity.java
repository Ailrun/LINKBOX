package org.sopt.linkbox.activity.loadingPage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.LinkBoxActivity;
import org.sopt.linkbox.constant.LoginStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UserData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.MainServerWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;
import org.sopt.linkbox.service.LinkHeadService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginLoadingActivity extends Activity {
    private static String TAG = "TEST/" + LoginLoadingActivity.class.getName() + " : ";

    private MainServerWrapper mainServerWrapper = null;
    private String usrID = null;
    private String usrName = null;
    private String usrProfile = null;
    private String usrPassword = null;
    private boolean facebook = false;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindow();
        initInterface();
        initData();
        initView();
        initListener();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_login_loading);
    }
    private void initInterface() {
        mainServerWrapper = new MainServerWrapper();
    }
    private void initData() {
        Intent intent = getIntent();
        usrID = intent.getStringExtra(LoginStrings.usrID);
        usrName = intent.getStringExtra(LoginStrings.usrName);
        usrProfile = intent.getStringExtra(LoginStrings.usrProfile);
        usrPassword = intent.getStringExtra(LoginStrings.usrPassword);
        facebook = intent.getBooleanExtra(LoginStrings.facebook, false);
        sharedPreferences = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        editor = sharedPreferences.edit();
    }
    private void initView() {
    }
    private void initListener() {
        if (facebook) {
            mainServerWrapper.postFacebookAccessAsync(usrID, usrName, usrProfile, usrPassword, new FacebookAccessCallback());
        }
        else {
            mainServerWrapper.postLoginAsync(usrID, usrPassword, new LoginCallback());
        }
    }

    private class LoginCallback implements Callback<MainServerData<UserData>> {
        @Override
        public void success(MainServerData<UserData> wrappedUserData, Response response) {
            if (wrappedUserData.result) {
                UserData userData = wrappedUserData.object;
                LinkBoxController.userData = userData;
                editor.putString(LoginStrings.usrID, userData.usrID);
                editor.putString(LoginStrings.usrPassword, userData.usrPassword);
                mainServerWrapper.getBoxListAsync(new BoxLoadingCallback());
            }
            else {
                Log.d(TAG, wrappedUserData.message);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class FacebookAccessCallback implements Callback<MainServerData<UserData>> {
        @Override
        public void success(MainServerData<UserData> wrappedUserData, Response response) {
            if (wrappedUserData.result) {
                UserData userData = wrappedUserData.object;
                LinkBoxController.userData = userData;
                Log.d(TAG, userData.toString() + " ");
                editor.putString(LoginStrings.usrID, userData.usrID);
                editor.putString(LoginStrings.usrPassword, userData.usrPassword);
                mainServerWrapper.getBoxListAsync(new BoxLoadingCallback());
            }
            else {
                Log.d(TAG, wrappedUserData.message);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class BoxLoadingCallback implements Callback<MainServerData<List<BoxListData>>> {
        @Override
        public void success(MainServerData<List<BoxListData>> wrappedBoxListDatas, Response response) {
            if (wrappedBoxListDatas.result) {
                List<BoxListData> boxListDatas = wrappedBoxListDatas.object;
                LinkBoxController.boxListSource = (ArrayList<BoxListData>) boxListDatas;
                if (boxListDatas.size() > 0) {
                    LinkBoxController.currentBox = boxListDatas.get(0);
                    mainServerWrapper.getUrlListAsync(new UrlLoadingCallback());
                }
                else {
                    Intent intent = new Intent(LoginLoadingActivity.this, LinkBoxActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
            else {
                Log.d(TAG, wrappedBoxListDatas.message);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class UrlLoadingCallback implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                List<UrlListData> urlListDatas = wrappedUrlListDatas.object;
                LinkBoxController.urlListSource = (ArrayList<UrlListData>) urlListDatas;
                Intent intent = new Intent(LoginLoadingActivity.this, LinkBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Log.d(TAG, wrappedUrlListDatas.message);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }
}
