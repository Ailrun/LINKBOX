package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.editorPage.BoxEditorAdd;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MinGu on 2015-09-18.
 */
public class BoxMenuActivity extends Activity {

    private static final String TAG = "TEST/" + BoxDeleteDialogActivity.class.getName() + " : ";

    private ImageView ivMenuClose = null;
    private TextView tvBoxShare = null;
    private TextView tvBoxEdit = null;
    private TextView tvBoxDelete = null;
    private BoxListData boxListData = null;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initWindow();
        initData();
        initView();
        initOnTouchListener();
        initListener();
    }
    private void initData() {
        index = getIntent().getIntExtra("boxIndex", 0);
        boxListData = LinkBoxController.boxListSource.get(index);
    }

    private void initWindow() {
        Window window = this.getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lpDialog = new WindowManager.LayoutParams();
        lpDialog.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpDialog.dimAmount = 0.7f;
        getWindow().setAttributes(lpDialog);
        setContentView(R.layout.layout_box_menu_dialog_link_box);

    }

    private void initView() {
        ivMenuClose = (ImageView) findViewById(R.id.IV_menu_close);
        tvBoxShare = (TextView) findViewById(R.id.TV_box_share);
        tvBoxEdit = (TextView) findViewById(R.id.TV_box_edit);
        tvBoxDelete = (TextView) findViewById(R.id.TV_box_delete);
    }

    private void initOnTouchListener(){
        tvBoxShare.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvBoxShare.setBackgroundColor(getResources().getColor(R.color.indigo500));
                    tvBoxShare.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    tvBoxShare.setBackgroundColor(getResources().getColor(R.color.real_white));
                    tvBoxShare.setTextColor(getResources().getColor(R.color.indigo500));
                }
                return false;
            }
        });
        tvBoxEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvBoxEdit.setBackgroundColor(getResources().getColor(R.color.indigo500));
                    tvBoxEdit.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    tvBoxEdit.setBackgroundColor(getResources().getColor(R.color.real_white));
                    tvBoxEdit.setTextColor(getResources().getColor(R.color.indigo500));
                }
                return false;
            }
        });
        tvBoxDelete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    tvBoxDelete.setBackgroundColor(getResources().getColor(R.color.indigo500));
                    tvBoxDelete.setTextColor(getResources().getColor(R.color.real_white));
                } else {
                    tvBoxDelete.setBackgroundColor(getResources().getColor(R.color.real_white));
                    tvBoxDelete.setTextColor(getResources().getColor(R.color.indigo500));
                }
                return false;
            }
        });
    }

    private void initListener() {

        ivMenuClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvBoxShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), BoxEditorAdd.class);
                startActivity(intent);
                finish();
            }
        });
        tvBoxEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxEditActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + index);
                intent.putExtra("boxIndex", index);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
        tvBoxDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BoxDeleteDialogActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Log.d(TAG, "BoxIndex : " + index);
                intent.putExtra("boxIndex", index);
                getApplicationContext().startActivity(intent);
                finish();
            }
        });
    }

}
