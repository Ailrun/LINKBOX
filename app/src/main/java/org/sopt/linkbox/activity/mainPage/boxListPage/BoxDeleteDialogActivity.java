package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.dialog.BoxDeleteDialog;
import org.sopt.linkbox.custom.dialog.DeleteDialog;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by sy on 2015-08-18.
 */
public class BoxDeleteDialogActivity extends Activity {
    private static final String TAG = "TEST/" + BoxDeleteDialogActivity.class.getName() + " : ";

    private Button bDelete = null;
    private Button bCancel = null;
    private BoxListWrapper boxListWrapper = null;
    private BoxListData boxListData = null;
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
        boxListWrapper = new BoxListWrapper();
    }
    private void initData() {
        index = getIntent().getIntExtra("boxIndex", 0);
        boxListData = LinkBoxController.boxListSource.get(index);
    }
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpDialog.dimAmount = 0.7f;
        getWindow().setAttributes(lpDialog);
        setContentView(R.layout.layout_box_delete_dialog_link_box);
    }
    private void initView() {
        bDelete = (Button) findViewById(R.id.B_delete_box_delete_dialog);
        bCancel = (Button) findViewById(R.id.B_cancel_box_delete_dialog);
    }
    private void initListener() {
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boxListWrapper.remove(boxListData, new BoxRemoveCallback(boxListData));
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private class BoxRemoveCallback implements Callback<MainServerData<Object>> {
        private BoxListData boxListData = null;

        public BoxRemoveCallback(BoxListData boxListData) {
            this.boxListData = boxListData;
        }
        @Override
        public void success(MainServerData<Object> wrapperObject, Response response) {
            if(wrapperObject.result) {
                LinkBoxController.boxListSource.remove(boxListData);
                LinkBoxController.notifyBoxDataSetChanged();
                finish();
            }
            else
            {
                Toast.makeText(BoxDeleteDialogActivity.this, "삭제에 실패했습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            Toast.makeText(BoxDeleteDialogActivity.this, "서버와 연결이 불안정합니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
