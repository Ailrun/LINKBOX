package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
public class DeleteDialogActivity extends Activity {
    private static final String TAG = "TEST/" + DeleteDialogActivity.class.getName() + " : ";

    private TextView tvTitle = null;
    private TextView tvContents = null;
    private Button bDelete = null;
    private Button bCancel = null;

    private UrlListWrapper urlListWrapper = null;
    private UrlListData urlListData = null;
    private int index = 0;
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
        setContentView(R.layout.layout_delete_dialog_link_box);
    }
    private void initData() {
        index = getIntent().getIntExtra("Index", 0);
        urlListData = LinkBoxController.urlListSource.get(index);
    }
    private void initView() {
        tvTitle = (TextView) findViewById(R.id.TV_title_delete_dialog);
        tvContents = (TextView) findViewById(R.id.TV_contents_delete_dialog);
        bDelete = (Button) findViewById(R.id.B_delete_delete_dialog);
        bCancel = (Button) findViewById(R.id.B_cancel_delete_dialog);
    }
    private void initListener() {
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlListWrapper.remove(urlListData, new UrlRemoveCallback(urlListData));
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }

    private class UrlRemoveCallback implements Callback<MainServerData<Object>> {
        private UrlListData urlListData = null;
        public UrlRemoveCallback(UrlListData urlListData) {
            this.urlListData = urlListData;
        }
        @Override
        public void success(MainServerData<Object> wrapperObject, Response response) {
            if(wrapperObject.result) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LinkBoxController.urlListSource.remove(urlListData);
                        LinkBoxController.notifyUrlDataSetChanged();
                    }
                }, 400);
                finish();

            }
            else
            {
                Log.d(TAG,"fail to delete");
                Toast.makeText(DeleteDialogActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            LinkBoxController.resetUrlDataSet();
            finish();
            Toast.makeText(DeleteDialogActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
        }
    }
    public void onBackPressed() {
        finish();

    }
}
