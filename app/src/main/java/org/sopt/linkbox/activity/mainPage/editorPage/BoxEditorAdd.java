package org.sopt.linkbox.activity.mainPage.editorPage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;
import org.sopt.linkbox.custom.helper.SessionSaver;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class BoxEditorAdd extends AppCompatActivity {
    private static final String TAG = "TEST/" + BoxEditorAdd.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private BoxListWrapper boxListWrapper = null;

    private Toolbar tToolbar = null;
    private EditText etEmail = null;
    private EditText tvMessage = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_editor_add);

        initInterface();
        initData();
        initView();
        initListener();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_editor_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_send :
                TwoString twoString = new TwoString();
                twoString.usrID = etEmail.getText().toString();
                twoString.message = tvMessage.getText().toString();
                boxListWrapper.invite(twoString, new BoxInviteCallback());
                break;
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        SessionSaver.saveSession(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initInterface() {
        boxListWrapper = new BoxListWrapper();
    }
    private void initData() {
    }
    private void initView() {
        initToolbarView();
        initMainView();
    }
    private void initListener() {
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Toolbar">
    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_add);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("에디터 추가");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Main">
    private void initMainView() {
        etEmail = (EditText) findViewById(R.id.ET_editor_email_editor_add);
        tvMessage = (EditText) findViewById(R.id.ET_message_box_editor_add);
        tvMessage.setHint(LinkBoxController.usrListData.usrName + "님이 당신을 '" + LinkBoxController.currentBox.boxName + "'박스 에 초대했습니다.");
    }
    //</editor-fold>

    //<editor-fold desc="Box Inner Classes" defaultstate="collapsed">
    private class BoxInviteCallback implements Callback<MainServerData<Object>> {
        @Override
        public void success(MainServerData<Object> objectMainServerData, Response response) {
            Toast.makeText(BoxEditorAdd.this, "Successfully Invite!", Toast.LENGTH_SHORT).show();
            finish();
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(BoxEditorAdd.this, "Fail to invite", Toast.LENGTH_SHORT).show();
            RetrofitDebug.debug(error);
        }
    }
    //</editor-fold>
}
