package org.sopt.linkbox.activity.mainPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.url.UrlListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/** T?O?D?O : make this as Single Instance
 * REFERENCE : http://www.androidpub.com/796480
 */
public class LinkItActivity extends Activity {
    private static final String TAG = "TEST/" + LinkItActivity.class.getName();

    //<editor-fold desc="Private Propeties" defaultstate="collapsed">
    private UrlListWrapper urlListWrapper = null;

    private Spinner sBox = null;
    private ImageView ivThumb = null;
    private EditText etName = null;
    private Button bLinkit = null, bCancel = null;
    private CheckBox cbReadLater = null;

    private UrlListData urlListData = null;
    private int checkedBox = 0;

    private TextView tvBlank;
    private TextView tvMessage;
    // Animation
    private Animation animSlideDown;
    private Animation animSlideUp;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);

        initInterface();

        initWindow();

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
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initInterface() {
        initServerInterface();
        initGlideInterface();
    }
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_link_it);
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
        initMainView();
        initAnimationView();
    }
    private void initListener() {
        initMainListener();
        initAnimationListener();
    }
    private void initControl() {
        LinkBoxController.linkItBoxListAdapter =
                new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        LinkBoxController.linkItBoxListAdapter = new LinkItBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        sBox.setAdapter(LinkBoxController.linkItBoxListAdapter);
    }
    private void initThumbnail() {
        Bundle parameter = new Bundle();

        parameter.putString("id", urlListData.url);
        parameter.putString("access_token", "1646442455642975|7bd84cfafd55d4e1fbf59c22a6030127");
        String path = "/v2.4/";
        GraphRequest graphRequest = new GraphRequest(null, path, parameter, HttpMethod.GET, new GetIDCallback());
        graphRequest.executeAsync();
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Server" defaultstate="collapsed">
    private void initServerInterface() {
        urlListWrapper = new UrlListWrapper();
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Glide" defaultstate="collapsed">
    private void initGlideInterface() {
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
    //</editor-fold>
    //<editor-fold desc="Initiate Main" defaultstate="collapsed">
    private void initMainView() {
        sBox = (Spinner) findViewById(R.id.S_box_link_it);
        etName = (EditText) findViewById(R.id.ET_name_link_it);
        etName.setHint(urlListData.url);
        ivThumb = (ImageView) findViewById(R.id.IV_thumb_link_it);
        cbReadLater = (CheckBox) findViewById(R.id.CB_box_link_it);
        bLinkit = (Button) findViewById(R.id.B_linkit_link_it);
        bLinkit.setEnabled(false);
        bCancel = (Button) findViewById(R.id.B_cancel_link_it);
    }
    private void initMainListener() {
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
        cbReadLater.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tvMessage.startAnimation(animSlideUp);
                    tvBlank.startAnimation(animSlideDown);
                } else {
                    tvMessage.startAnimation(animSlideDown);
                    tvBlank.startAnimation(animSlideUp);
                }
            }
        });
        bLinkit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlListData.urlWriterUsrKey = LinkBoxController.usrListData.usrKey;
                urlListData.urlWriterUsrName = LinkBoxController.usrListData.usrName;
                urlListData.urlTitle = etName.getText().toString();
                urlListData.urlDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
                urlListWrapper.add(urlListData, (BoxListData)sBox.getSelectedItem(), new UrlAddingCallback());
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Animation">
    private void initAnimationView() {
        tvMessage = (TextView) findViewById(R.id.TV_box_link_it);
        tvBlank = (TextView) findViewById(R.id.TV_blank);

        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_down);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_slide_up);
    }
    private void initAnimationListener() {
        animSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Thumbnail Inner Classes" defaultstate="collapsed">
    private class GetIDCallback implements GraphRequest.Callback {
        @Override
        public void onCompleted(GraphResponse graphResponse) {
            String json = graphResponse.getRawResponse();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                String id = jsonObject.optJSONObject("og_object").optString("id");
                Bundle parameter = new Bundle();
                parameter.putString("id", id);
                parameter.putString("access_token", "1646442455642975|7bd84cfafd55d4e1fbf59c22a6030127");
                String path = "/v2.4/";
                GraphRequest graphRequest = new GraphRequest(null, path, parameter, HttpMethod.POST, new GetThumbnailCallback());
                graphRequest.executeAsync();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class GetThumbnailCallback implements GraphRequest.Callback {
        @Override
        public void onCompleted(GraphResponse graphResponse) {
            bLinkit.setEnabled(true);
            Log.d(TAG, graphResponse.toString());
            String json = graphResponse.getRawResponse();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(json);
                JSONArray jsonArray = jsonObject.getJSONArray("image");
                urlListData.urlThumbnail = jsonArray.getJSONObject(0).getString("url");
                Glide.with(LinkItActivity.this).load(urlListData.urlThumbnail).into(ivThumb);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="URL Inner Classes" defaultstate="collapsed">
    private class UrlAddingCallback implements Callback<MainServerData<UrlListData>> {
        @Override
        public void success(MainServerData<UrlListData> wrappedUrlListData, Response response) {
            if (wrappedUrlListData.result) {
                LinkBoxController.urlListSource.add(0, wrappedUrlListData.object);
                LinkBoxController.notifyUrlDataSetChanged();
                finish();
            }
            else {
                Toast.makeText(LinkItActivity.this, "There is some error in adding url", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
            finish();
        }
    }
    //</editor-fold>
}
