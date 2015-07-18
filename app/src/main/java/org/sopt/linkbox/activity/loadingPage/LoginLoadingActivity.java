package org.sopt.linkbox.activity.loadingPage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import org.sopt.linkbox.custom.network.MainServerWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;
import org.sopt.linkbox.service.LinkHeadService;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class LoginLoadingActivity extends Activity {
    private MainServerWrapper mainServerWrapper = null;
    private String usremail = null;
    private String usrname = null;
    private String usrprofile = null;
    private String pass = null;
    private boolean isFacebook = false;

    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
        initWindow();
        initInterface();
        initData();
        initView();
        initListener();
    }
    @Override
    public void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
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
        usremail = intent.getStringExtra(LoginStrings.usremail);
        usrname = intent.getStringExtra(LoginStrings.usrname);
        usrprofile = intent.getStringExtra(LoginStrings.usrprofile);
        pass = intent.getStringExtra(LoginStrings.pass);
        isFacebook = intent.getBooleanExtra(LoginStrings.isfacebook, false);
        sharedPreferences = getSharedPreferences(SettingStrings.shared_user_profiles, 0);
        editor = sharedPreferences.edit();
    }
    private void initView() {

    }
    private void initListener() {
        if (isFacebook) {
            mainServerWrapper.postFacebookAccessAsync(usremail, usrname, usrprofile, pass, new FacebookAccessCallback());
        }
        else {
            mainServerWrapper.postLoginAsync(usremail, pass, new LoginCallback());
        }
    }

    private class LoginCallback implements Callback<UserData> {
        @Override
        public void success(UserData userData, Response response) {
            LinkBoxController.userData = userData;
            editor.putString(LoginStrings.usremail, userData.usremail);
            editor.putString(LoginStrings.pass, userData.pass);
            mainServerWrapper.getBoxListAsync(new BoxLoadingCallback());
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class FacebookAccessCallback implements Callback<UserData> {
        @Override
        public void success(UserData userData, Response response) {
            LinkBoxController.userData = userData;
            editor.putString(LoginStrings.usremail, userData.usremail);
            editor.putString(LoginStrings.pass, userData.pass);
            mainServerWrapper.getBoxListAsync(new BoxLoadingCallback());
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class BoxLoadingCallback implements Callback<List<BoxListData>> {
        @Override
        public void success(List<BoxListData> boxListDatas, Response response) {
            LinkBoxController.boxListSource = (ArrayList<BoxListData>) boxListDatas;
            mainServerWrapper.getUrlListAsync(new UrlLoadingCallback());
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }

    private class UrlLoadingCallback implements Callback<List<UrlListData>> {
        @Override
        public void success(List<UrlListData> urlListDatas, Response response) {
            LinkBoxController.urlListSource = (ArrayList<UrlListData>) urlListDatas;
            Intent intent = new Intent(LoginLoadingActivity.this, LinkBoxActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }
}
