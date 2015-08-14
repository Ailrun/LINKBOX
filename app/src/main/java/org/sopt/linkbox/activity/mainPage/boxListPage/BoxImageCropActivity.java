package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.edmodo.cropper.CropImageView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.helper.ImageSaveLoad;

/**
 * Created by MinGu on 2015-08-13.
 */
public class BoxImageCropActivity extends AppCompatActivity {
    private CropImageView cropImageView = null;
    private Button accept = null;
    private Button decline = null;
    private Button ratio = null;
    protected final int SELECT_GALLERY = 1;
    private Uri imgURI = null;
    private Bitmap bmp = null;
    private ImageSaveLoad imageSaveLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imageSaveLoader = new ImageSaveLoad(getApplicationContext());

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_GALLERY);

        setContentView(R.layout.activity_box_image_crop);

        initView();
        // initControl();
        initListener();
    }

    public void initView(){
        cropImageView = (CropImageView) findViewById(R.id.CIV_crop_image);
        // cropImageView.setAspectRatio(1, 1);
        // cropImageView.setFixedAspectRatio(true);

        accept = (Button) findViewById(R.id.B_accept);
        decline = (Button) findViewById(R.id.B_decline);
        ratio = (Button) findViewById(R.id.B_ratio);
        ratio.setText("Free");
    }
    /*
    public void initControl(){
        cropImageView.setImageBitmap(LinkBoxController.temporaryImage);
    }
    */
    public void initListener(){
        accept.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                LinkBoxController.boxImage = cropImageView.getCroppedImage();
                // String saveStatus = .saveProfileImage(LinkBoxController.userImage);
                // Log.d("Save Status : ", saveStatus);]
                finish();

            }
        });
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ratio.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(ratio.getText().toString().compareTo("Free") == 0){
                    cropImageView.setAspectRatio(1, 1);
                    cropImageView.setFixedAspectRatio(true);
                    ratio.setText("1:1");
                }
                else{
                    cropImageView.setFixedAspectRatio(false);
                    ratio.setText("Free");
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {

            try {
                imgURI = data.getData();
                // ivProfile.setImageURI(imgURI);
                // filePath = getRealPathFromURI(imgURI);
                bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);
                // LinkBoxController.temporaryImage = bmp;
                // ivProfile.setImageBitmap(bmp);
                // ivProfile.getCroppedBitmap(bmp, 15);
                // ivProfile.setCropToPadding(true);
                cropImageView.setImageBitmap(bmp);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            if(LinkBoxController.boxImage != null){
                cropImageView.setImageBitmap(LinkBoxController.boxImage);
            }
            else{
                finish();
            }
        }
    }


}
