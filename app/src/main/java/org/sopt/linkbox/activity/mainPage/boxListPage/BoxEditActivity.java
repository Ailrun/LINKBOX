package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;
import org.sopt.linkbox.custom.network.main.box.BoxListWrapper;
import org.sopt.linkbox.debugging.RetrofitDebug;

import java.io.File;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by MinGu on 2015-08-13.
 * 
 */
public class BoxEditActivity extends Activity {
    private static final String TAG = "TEST/" + BoxEditActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private BoxListWrapper boxListWrapper = null;

    private ImageView ibThumb = null;
    private EditText etName = null;
    private Button bSave = null, bCancel = null;
    private BoxListData boxListData = null;
    private BoxImageSaveLoad boxImageSaveLoader = null;
    private int index;
    private String boxName = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boxImageSaveLoader = new BoxImageSaveLoad(getApplicationContext());

        initInterface();
        initWindow();
        initData();
        initView();
        initListener();
    }
    @Override
    protected void onResume() {
        super.onResume();
        /*
        if( boxImageSaveLoader.loadProfileImage(index) != null) {
            Bitmap bmp = boxImageSaveLoader.loadProfileImage(index);
            ibThumb.setImageBitmap(bmp);
        }*/
        if(LinkBoxController.boxImage != null){
            // Bitmap bmp = boxImageSaveLoader.loadProfileImage(LinkBoxController.currentBox.boxKey);
            ibThumb.setImageBitmap(LinkBoxController.boxImage);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
        setContentView(R.layout.activity_box_edit);
    }
    private void initData() {
        index = getIntent().getIntExtra("boxIndex", 0);
    }
    private void initView() {
        boxListData = LinkBoxController.boxListSource.get(index);
        etName = (EditText) findViewById(R.id.ET_box_name_box_add);
        etName.setText(boxListData.boxName);
        ibThumb = (ImageView) findViewById(R.id.IB_thumbnail_box_add);
        bSave = (Button) findViewById(R.id.B_save_box_add);
        bCancel = (Button) findViewById(R.id.B_cancel_box_add);
    }
    private void initListener() {
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BoxListData box = boxListData.clone();
                boxName = etName.getText().toString();
                box.boxName = boxName;
                boxListWrapper.edit(box, new BoxEditCallback(LinkBoxController.boxListSource.get(index)));
                LinkBoxController.boxImage = null;
            }
        });
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                LinkBoxController.boxImage = null;
            }
        });
        ibThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BoxEditActivity.this, BoxImageCropActivity.class);
                startActivity(intent);
            }
        });
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Server" defaultstate="collapsed">
    private void initServerInterface() {
        boxListWrapper = new BoxListWrapper();
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

    //<editor-fold desc="Box Inner Classes" defaultstate="collapsed">
    private class BoxEditCallback implements Callback<MainServerData<Object>> {
        BoxListData boxListData = null;
        public BoxEditCallback(BoxListData boxListData) {
            this.boxListData = boxListData;
        }
        @Override
        public void success(MainServerData<Object> wrappedObject, Response response) {
            if (wrappedObject.result) {
                boxListData.boxName = boxName;
                // boxListData.boxThumbnail = boxImageSaveLoader.saveProfileImage(ibThumb.getDrawingCache(), boxListData.boxIndex);
                Drawable drawable = ibThumb.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap bitmap = bitmapDrawable.getBitmap();
                LinkBoxController.boxListSource.get(index).boxThumbnail = boxImageSaveLoader.saveProfileImage(bitmap, index);
                LinkBoxController.notifyBoxDataSetChanged();
                finish();
            }
            else {
                Toast.makeText(BoxEditActivity.this, "Fail to edit Box", Toast.LENGTH_LONG).show();
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
