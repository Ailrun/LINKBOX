package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Junyoung on 2015-08-18.
 *
 */
public class EditDialogActivity extends Activity {
    private static final String TAG = "TEST/" + EditDialogActivity.class.getName() + " : ";

    private TextView tvName = null;
    private EditText etTitle = null;
    private Button bEdit = null;
    private Button bCancel = null;

    private UrlListWrapper urlListWrapper = null;
    private UrlListData urlListData = null;
    private int index = 0;
    private String urlTitle = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInterface();
        initWindow();
        initData();
        initView();
        initListener();
    }

    private void initInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpDialog.dimAmount = 0.7f;
        getWindow().setAttributes(lpDialog);
        setContentView(R.layout.layout_edit_dialog_link_box);
    }
    private void initData() {
        index = getIntent().getIntExtra("Index", 0);
        urlListData = LinkBoxController.urlListSource.get(index);
    }
    private void initView() {
        tvName = (TextView) findViewById(R.id.TV_name_edit_dialog);
        etTitle = (EditText) findViewById(R.id.ET_title_edit_dialog);
        bEdit = (Button) findViewById(R.id.B_edit_link_apply_edit_dialog);
        bCancel = (Button) findViewById(R.id.B_edit_link_cancel_edit_dialog);
        if (urlListData.urlWriterUsrName != null) {
            etTitle.setText(urlListData.urlWriterUsrName);
        }
    }
    private void initListener() {
        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlTitle = etTitle.getText().toString();
                urlListWrapper.edit(urlListData, urlTitle, new UrlEditCallback(urlListData));
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            }
        });
    }

    private class UrlEditCallback implements Callback<MainServerData<Object>> {
        private UrlListData urlListData = null;
        public UrlEditCallback(UrlListData urlListData) {
            this.urlListData = urlListData;
        }
        @Override
        public void success(MainServerData<Object> wrapperObjects, Response response) {
            if(wrapperObjects.result) {
                urlListData.urlTitle = urlTitle;
                LinkBoxController.notifyUrlDataSetChanged();
                finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
            }
            else {
                Log.d(TAG, "fail to Logout");
                Toast.makeText(EditDialogActivity.this, "로그아웃이 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            LinkBoxController.resetUrlDataSet();
            finish();
            Toast.makeText(EditDialogActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }
}
