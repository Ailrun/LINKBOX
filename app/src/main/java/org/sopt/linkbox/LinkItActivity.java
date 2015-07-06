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

import org.sopt.linkbox.custom.adapters.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.LinkItBoxListData;
import org.sopt.linkbox.service.LinkHeadService;

import java.util.ArrayList;


public class LinkItActivity extends Activity {

    private Spinner spBox = null;
    private ImageView ivThumb = null;
    private EditText etTitle = null;
    private Button btLinkit = null, btCancel = null;

    private String boxName = null;
    private String linkName = null;

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

    private ArrayList<LinkItBoxListData> source = null;

    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            linkName = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else {
            linkName = "";
        }
        source = new ArrayList<>();
        LinkItBoxListData linkItBoxListData = new LinkItBoxListData();
        linkItBoxListData.boxName = "요리";
        source.add(linkItBoxListData);
        linkItBoxListData = new LinkItBoxListData();
        linkItBoxListData.boxName = "아이";
        source.add(linkItBoxListData);
    }

    private void initView() {
        spBox = (Spinner) findViewById(R.id.SP_box_link_it);
        ivThumb = (ImageView) findViewById(R.id.IV_thumb_link_it);
        etTitle = (EditText) findViewById(R.id.ET_title_link_it);
        etTitle.setHint(linkName);
        btLinkit = (Button) findViewById(R.id.BT_linkit_link_it);
        btCancel = (Button) findViewById(R.id.BT_cancel_link_it);
    }

    private void initListener() {
        spBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                boxCheck(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                boxCheck(adapterView, 0);
            }

            private void boxCheck(AdapterView<?> adapterView, int i) {
                LinkItBoxListData linkItBoxListData = (LinkItBoxListData) adapterView.getItemAtPosition(i);
                if (linkItBoxListData != null) {
                    boxName = linkItBoxListData.boxName;
                } else {
                    boxName = "";
                }
            }
        });
        btLinkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(getApplicationContext(), LinkHeadService.class));
                finish();
            }
        });
    }

    private LinkItBoxListAdapter linkItBoxListAdapter = null;

    private void initControl() {
        linkItBoxListAdapter = new LinkItBoxListAdapter(getApplicationContext(), source);
        spBox.setAdapter(linkItBoxListAdapter);
    }
}
