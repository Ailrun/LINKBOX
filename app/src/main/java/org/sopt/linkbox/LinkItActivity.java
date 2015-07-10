package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.custom.adapters.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.LinkBoxListData;
import org.sopt.linkbox.custom.data.LinkUrlListData;
import org.sopt.linkbox.custom.network.LinkNetwork;
import org.sopt.linkbox.service.LinkHeadService;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/** T?O?D?O : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
public class LinkItActivity extends Activity {
    private static final String TAG = "TEST/" + LinkItActivity.class.getName();

    private Spinner spBox = null;
    private ImageView ivThumb = null;
    private EditText etName = null;
    private Button btLinkit = null, btCancel = null;

    private String boxName = null;
    private LinkUrlListData linkUrlListData = null;

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stopService(new Intent(getApplicationContext(), LinkHeadService.class));

        initWindow();

        initGlide();

        initData();

        initView();
        initListener();
        initControl();
    }
    @Override
    public void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (sharedPreferences.getBoolean("floating", true)) {
            startService(new Intent(getApplicationContext(), LinkHeadService.class));
        }
    }

    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_link_it);
    }
    private void initGlide() {

        synchronized (Glide.class){
            if(!Glide.isSetup()){
                File file = Glide.getPhotoCacheDir(getApplicationContext());
                int size = 1024*1024*1024;
                DiskCache cache = DiskLruCacheWrapper.get(file, size);
                GlideBuilder builder = new GlideBuilder(getApplicationContext());
                builder.setDiskCache(cache);
                Glide.setup(builder);
            }
        }
    }
    private void initData() {
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.shared_profile)
                + LinkBoxController.linkUserData.usrid, 0);
        linkUrlListData = new LinkUrlListData();
    }
    private void initView() {
        spBox = (Spinner) findViewById(R.id.SP_box_link_it);
        ivThumb = (ImageView) findViewById(R.id.IV_thumb_link_it);
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            linkUrlListData.address = intent.getStringExtra(Intent.EXTRA_TEXT);
            LinkNetwork.Embedly.getThumbUrlFromEmbedlyAsync(linkUrlListData, ivThumb);
        }
        else {
            finish();
            Log.e(TAG, "There is no address but LinkItActivity start");
        }
        etName = (EditText) findViewById(R.id.ET_name_link_it);
        etName.setHint(linkUrlListData.address);
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
                linkUrlListData.urlname = etName.getText().toString();
                linkUrlListData.isgood = false;
                linkUrlListData.urlwriter = LinkBoxController.linkUserData.usrname;
                linkUrlListData.urldate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                LinkNetwork.Server.postAddUrlToServerAsync(linkUrlListData);
                Intent intent = new Intent(getApplicationContext(), LinkBoxActivity.class);
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
        LinkBoxController.linkItBoxListAdapter =
                new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        LinkBoxController.linkItBoxListAdapter = new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        spBox.setAdapter(LinkBoxController.linkItBoxListAdapter);
    }
}
