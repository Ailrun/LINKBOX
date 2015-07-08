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
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.service.LinkHeadService;


/** TODO : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
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
        initWindow();

        initData();

        initView();
        initListener();
        initControl();
    }

    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
        setContentView(R.layout.activity_link_it);
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            linkName = intent.getStringExtra(Intent.EXTRA_TEXT);
        }
        else {
            linkName = "";
        }
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
                LinkBoxListData linkBoxListData = (LinkBoxListData) adapterView.getItemAtPosition(i);
                if (linkBoxListData != null) {
                    boxName = linkBoxListData.cbname;
                } else {
                    boxName = "";
                }
            }
        });
        btLinkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LinkBoxActivity.class);
                intent.putExtra("cbid", 1);
                intent.putExtra("cbname", boxName);
                intent.putExtra("urlname", etTitle.getText().toString());
                startActivity(intent);
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
    private void initControl() {
        LinkBoxController.linkItBoxListAdapter = new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        spBox.setAdapter(LinkBoxController.linkItBoxListAdapter);
    }
}
