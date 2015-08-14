package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.custom.adapters.spinnerAdapter.LinkItBoxListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BoxAddActivity extends Activity {

    private BoxListWrapper boxListWrapper = null;

    private ImageView ibThumb = null;
    private EditText etName = null;
    private Button bSave = null, bCancel = null;
    private BoxListData box = null;
    private BoxImageSaveLoad boxImageSaveLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boxImageSaveLoader = new BoxImageSaveLoad(getApplicationContext());
        initInterface();
        initWindow();
        initGlide();
        initView();
        initListener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(LinkBoxController.boxImage != null){
            // Bitmap bmp = boxImageSaveLoader.loadProfileImage(LinkBoxController.currentBox.boxKey);
            ibThumb.setImageBitmap(LinkBoxController.boxImage);
        }
    }

    private void initInterface() {
        boxListWrapper = new BoxListWrapper();
    }
    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_box_add);
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
    private void initView() {
        etName = (EditText) findViewById(R.id.ET_box_name_box_add);
        ibThumb = (ImageView) findViewById(R.id.IB_thumbnail_box_add);
        bSave = (Button) findViewById(R.id.B_save_box_add);
        bCancel = (Button) findViewById(R.id.B_cancel_box_add);
    }
    private void initListener() {
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                box = new BoxListData();
                int boxIndex = LinkBoxController.boxListSource.size();
                box.boxIndex = boxIndex;
                box.boxName = etName.getText().toString();
                box.isFavorite = 0;
                box.boxUrlNum = 0;
                boxListWrapper.add(box, new BoxAddingCallback());
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ibThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoxAddActivity.this, BoxImageCropActivity.class);
                startActivity(intent);
            }
        });
    }

    private class BoxAddingCallback implements Callback<MainServerData<BoxListData>> {
        @Override
        public void success(MainServerData<BoxListData> wrappedBoxListData, Response response) {
            if (wrappedBoxListData.result) {
                box.boxKey = wrappedBoxListData.object.boxKey;
                LinkBoxController.boxListSource.add(box);
                box.boxThumbnail = boxImageSaveLoader.saveProfileImage(ibThumb.getDrawingCache(), box.boxIndex);
                finish();
            }
            else {
                Toast.makeText(BoxAddActivity.this, "Fail to add Box", Toast.LENGTH_LONG).show();
                finish();
            }
        }
        @Override
        public void failure(RetrofitError error) {
            RetrofitDebug.debug(error);
        }
    }
}
