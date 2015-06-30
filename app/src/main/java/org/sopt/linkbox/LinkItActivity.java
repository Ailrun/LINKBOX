package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.ArrayList;


public class LinkItActivity extends Activity {

    Spinner sp_box = null;
    ImageView iv_thumb = null;
    EditText et_title = null;
    Button bt_linkit = null, bt_cancel = null;

    private String box_name = null;
    private String link_name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_link_it);
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));

        initData();
        initView();
        initListener();
        initControl();
    }

    ArrayList<BoxListData> source = null;

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            link_name = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else {
            link_name = "";
        }
        source = new ArrayList<>();
        BoxListData boxListData = new BoxListData();
        boxListData.box_name = "요리";
        source.add(boxListData);
        boxListData = new BoxListData();
        boxListData.box_name = "아이";
        source.add(boxListData);
    }

    private void initView() {
        sp_box = (Spinner) findViewById(R.id.SP_BOX);
        iv_thumb = (ImageView) findViewById(R.id.IV_THUMB);
        et_title = (EditText) findViewById(R.id.ET_TITLE);
        et_title.setText(link_name);
        bt_linkit = (Button) findViewById(R.id.BT_LINKIT);
        bt_cancel = (Button) findViewById(R.id.BT_CANCEL);
    }

    BoxListAdapter boxListAdapter = null;

    private void initListener() {
        sp_box.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                boxCheck(adapterView, i);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                boxCheck(adapterView, 0);
            }
            private void boxCheck(AdapterView<?> adapterView, int i) {
                BoxListData boxListData = (BoxListData) adapterView.getItemAtPosition(i);
                if (boxListData != null) {
                    box_name = boxListData.box_name;
                }
                else {
                    box_name = "";
                }
            }
        });
        bt_linkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), LinkHeadService.class));
                finish();
            }
        });
    }

    private void initControl() {
        boxListAdapter = new BoxListAdapter(getApplicationContext(), source);
        sp_box.setAdapter(boxListAdapter);
    }
}
