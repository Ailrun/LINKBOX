package org.sopt.linkbox.activity.loadingPage;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.helpPage.TutorialActivity;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.constant.UsrType;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.custom.network.main.usr.UsrListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AccountLoadingActivity extends Activity {
    private static final String TAG = "TEST/" + AccountLoadingActivity.class.getName() + " : ";
    private static final int PROGRESS_DIALOG = 1001;

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private ProgressDialog progressDialog;
    private UsrListWrapper usrListWrapper = null;
    private BoxListWrapper boxListWrapper = null;
    private UrlListWrapper urlListWrapper = null;

    private String usrID = null;
    private String usrName = null;
    private String usrPassword = null;
    private int usrType = 0;

    private SharedPreferences pref = null;
    private int login_count = 0;

    private SharedPreferences.Editor speProfile = null;
    //</editor-fold>

    //<editor-fold desc="Overried Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showDialog(PROGRESS_DIALOG);
        initPreference();
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
    private void initPreference() {
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (pref != null) {
            login_count = pref.getInt("login_count", 0);

        }
    }
    @Override
    public Dialog onCreateDialog(int id)
    {
        switch (id){
            case (PROGRESS_DIALOG):
                progressDialog = new ProgressDialog(this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("로그인 중입니다.");

                return progressDialog;
        }
        return null;
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_login_loading);
    }
    private void initInterface() {
        usrListWrapper = new UsrListWrapper();
        boxListWrapper = new BoxListWrapper();
        urlListWrapper = new UrlListWrapper();
    }
    private void initData() {
        Intent intent = getIntent();
        usrID = intent.getStringExtra(AccountStrings.usrID);
        usrName = intent.getStringExtra(AccountStrings.usrName);
        usrPassword = intent.getStringExtra(AccountStrings.usrPassword);
        usrType = intent.getIntExtra(AccountStrings.usrType, UsrType.normal_user);
        speProfile = getSharedPreferences(SettingStrings.shared_user_profiles, 0).edit();
    }
    private void initView() {
    }
    private void initListener() {
        switch (usrType) {
            case UsrType.new_user :
                usrListWrapper.signup(usrID, usrName, usrPassword, UsrType.normal_user, new SignupCallback());
                break;
            case UsrType.normal_user :
                usrListWrapper.login(usrID, usrPassword, UsrType.normal_user, new LoginCallback());
                break;
            case UsrType.facebook_user :
                usrListWrapper.login(usrID, usrPassword, UsrType.facebook_user, new FacebookLoginCallback());
                break;
            case UsrType.google_user :
                //TODO:Add google login
                break;
        }
    }
    //</editor-fold>

    //<editor-fold desc="User Inner Classes" defaultstate="collapsed">
    private class FacebookLoginCallback implements Callback<MainServerData<UsrListData>> {
        @Override
        public void success(MainServerData<UsrListData> wrappedUserData, Response response) {
            if (wrappedUserData.result) {
                LinkBoxController.usrListData = new UsrListData();
                UsrListData usrListData = wrappedUserData.object;
                LinkBoxController.usrListData = usrListData;
                Log.d(TAG, usrListData.toString() + " ");
                speProfile.putString(AccountStrings.usrID, usrListData.usrID);
                speProfile.putString(AccountStrings.usrPassword, usrListData.usrPassword);
                boxListWrapper.list(new BoxLoadingCallback());
            }
            else {
                usrListWrapper.signup(usrID, usrName, usrPassword, UsrType.facebook_user, new SignupCallback());
                Log.d(TAG, wrappedUserData.message);
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(AccountLoadingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private class LoginCallback implements Callback<MainServerData<UsrListData>> {
        @Override
        public void success(MainServerData<UsrListData> wrappedUserData, Response response) {  // Server has succeeded in interacting with the Database.
            if (wrappedUserData.result) {   // Checks if the value for id and password exists in the server database
                LinkBoxController.usrListData = new UsrListData();
                UsrListData usrListData = wrappedUserData.object;
                LinkBoxController.usrListData = usrListData;
                speProfile.putString(AccountStrings.usrID, usrListData.usrID);
                speProfile.putString(AccountStrings.usrPassword, usrListData.usrPassword);
                speProfile.apply();
                boxListWrapper.list(new BoxLoadingCallback());

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("login_count", 1);
                editor.commit();
            }
            else {
                Log.d(TAG, wrappedUserData.message);
                Toast.makeText(AccountLoadingActivity.this, "존재하지 않는 아이디거나 비밀번호를 잘못 입력하셨습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(AccountLoadingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    private class SignupCallback implements Callback<MainServerData<UsrListData>> {
        @Override
        public void success(MainServerData<UsrListData> wrappedUsrListData, Response response) {
            if (wrappedUsrListData.result) {
                UsrListData usrListData = wrappedUsrListData.object;
                LinkBoxController.usrListData = usrListData;
                speProfile.putString(AccountStrings.usrID, usrListData.usrID);
                speProfile.putString(AccountStrings.usrPassword, usrListData.usrPassword);
                speProfile.apply();
                boxListWrapper.list(new BoxLoadingCallback());
            }
            else {
                Log.d(TAG, wrappedUsrListData.message);
                Toast.makeText(AccountLoadingActivity.this, "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(AccountLoadingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //</editor-fold>
    //<editor-fold desc="Box Inner Classes" defaultstate="collapsed">
    private class BoxLoadingCallback implements Callback<MainServerData<List<BoxListData>>> {
        @Override
        public void success(MainServerData<List<BoxListData>> wrappedBoxListDatas, Response response) {
            if (wrappedBoxListDatas.result) {
                List<BoxListData> boxListDatas = wrappedBoxListDatas.object;
                LinkBoxController.boxListSource = (ArrayList<BoxListData>) boxListDatas;
                if (boxListDatas.size() > 0) {
                    LinkBoxController.currentBox = boxListDatas.get(0);
                    urlListWrapper.allList(0, 100, new UrlLoadingCallback());
                }
                else {
                    Intent intent;
                    if(login_count != 0) {
                        intent = new Intent(AccountLoadingActivity.this, LinkBoxActivity.class);
                    }
                    else {
                        intent = new Intent(AccountLoadingActivity.this, TutorialActivity.class);
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
            else {
                Log.d(TAG, wrappedBoxListDatas.message);
                Toast.makeText(AccountLoadingActivity.this, "박스 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Intent intent;
                if(login_count != 0) {
                    intent = new Intent(AccountLoadingActivity.this, LinkBoxActivity.class);
                }
                else {
                    intent = new Intent(AccountLoadingActivity.this, TutorialActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(AccountLoadingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //</editor-fold>
    //<editor-fold desc="URL Inner Classes" defaultstate="collapsed">
    private class UrlLoadingCallback implements Callback<MainServerData<List<UrlListData>>> {
        @Override
        public void success(MainServerData<List<UrlListData>> wrappedUrlListDatas, Response response) {
            if (wrappedUrlListDatas.result) {
                List<UrlListData> urlListDatas = wrappedUrlListDatas.object;
                LinkBoxController.urlListSource = (ArrayList<UrlListData>) urlListDatas;
                Intent intent;
                if(login_count != 0) {
                    intent = new Intent(AccountLoadingActivity.this, LinkBoxActivity.class);
                }
                else {
                    intent = new Intent(AccountLoadingActivity.this, TutorialActivity.class);
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            else {
                Log.d(TAG, wrappedUrlListDatas.message);
                Toast.makeText(AccountLoadingActivity.this, "URL 로딩에 실패했습니다.", Toast.LENGTH_SHORT).show();
                Intent intent;
                if(login_count != 0) {
                    intent = new Intent(AccountLoadingActivity.this, LinkBoxActivity.class);
                }
                else {
                    intent = new Intent(AccountLoadingActivity.this, TutorialActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            Toast.makeText(AccountLoadingActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //</editor-fold>
}
