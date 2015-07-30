package org.sopt.linkbox.activity.mainPage;

import android.app.Activity;
import android.content.Intent;
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
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.mainData.UrlListData;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


/** T?O?D?O : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
public class LinkItActivity extends Activity {
    private static final String TAG = "TEST/" + LinkItActivity.class.getName();

    private Spinner sBox = null;
    private ImageView ivThumb = null;
    private EditText etName = null;
    private Button bLinkit = null, bCancel = null;

    private UrlListData urlListData = null;
    private int checkedBox = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

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
    }
    @Override
    protected void onStop() {
        super.onStop();
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
        urlListData = new UrlListData();
        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            urlListData.url = intent.getStringExtra(Intent.EXTRA_TEXT);
            initThumbnail();
        }
        else {
            finish();
            Log.e(TAG, "There is no url but LinkItActivity start");
        }
    }
    private void initView() {
        sBox = (Spinner) findViewById(R.id.S_box_link_it);
        etName = (EditText) findViewById(R.id.ET_name_link_it);
        etName.setHint(urlListData.url);
        ivThumb = (ImageView) findViewById(R.id.IV_thumb_link_it);
        bLinkit = (Button) findViewById(R.id.B_linkit_link_it);
        bCancel = (Button) findViewById(R.id.B_cancel_link_it);
    }
    private void initListener() {
        sBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                boxCheck(adapterView, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                boxCheck(adapterView, 0);
            }

            private void boxCheck(AdapterView<?> adapterView, int i) {
                checkedBox = i;
            }
        });
        bLinkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlListData.urlTitle = etName.getText().toString();
                urlListData.urlDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                Intent intent = new Intent(getApplicationContext(), LinkBoxActivity.class);
                startActivity(intent);
                finish();
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initControl() {
        LinkBoxController.linkItBoxListAdapter =
                new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        LinkBoxController.linkItBoxListAdapter = new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        sBox.setAdapter(LinkBoxController.linkItBoxListAdapter);
    }

    private void initThumbnail() {
        String path = "?id=" + urlListData.url;

        GraphRequest graphRequest = GraphRequest.newGraphPathRequest(null, path, new GraphCallback());
    }

    private class GraphCallback implements GraphRequest.Callback {
        @Override
        public void onCompleted(GraphResponse graphResponse) {
            Log.d(TAG, graphResponse.getJSONObject().toString());
        }
    }
}
