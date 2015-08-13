package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.PhotoCropActivity;
import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.helper.BoxImageSaveLoad;

import java.io.File;

/**
 * Created by MinGu on 2015-08-13.
 */
public class BoxEditActivity extends Activity {

    private ImageView ibThumb = null;
    private EditText etName = null;
    private Button bSave = null, bCancel = null;
    private BoxListData box = null;
    private BoxImageSaveLoad boxImageSaveLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initWindow();

        super.onCreate(savedInstanceState);
        boxImageSaveLoader = new BoxImageSaveLoad(getApplicationContext());

        initGlide();

        initView();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(LinkBoxController.currentBox != null){
            // Bitmap bmp = boxImageSaveLoader.loadProfileImage(LinkBoxController.currentBox.boxKey);
            ibThumb.setImageBitmap(LinkBoxController.boxImage);
            LinkBoxController.boxImage = null;
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


    private void initWindow() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.7f;
        getWindow().setAttributes(layoutParams);
        setContentView(R.layout.activity_box_edit);
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
                box = LinkBoxController.currentBox;
                LinkBoxController.boxListSource.get(box.boxIndex).boxName = etName.getText().toString();
                Drawable drawable = ibThumb.getDrawable();
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) drawable);
                Bitmap bitmap = bitmapDrawable.getBitmap();

                LinkBoxController.boxListSource.get(box.boxIndex).boxThumbnail = boxImageSaveLoader.saveProfileImage(bitmap, box.boxIndex);
                finish();
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
                Intent intent = new Intent(BoxEditActivity.this, BoxImageCropActivity.class);
                startActivity(intent);
            }
        });
    }

}
