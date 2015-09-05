package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.edmodo.cropper.CropImageView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-13.
 *
 */
public class BoxImageCropActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + BoxImageCropActivity.class.getName() + " : ";

    private final int SELECT_GALLERY = 1;

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private CropImageView cropImageView = null;
    private Button accept = null;
    private Button decline = null;
    private Button ratio = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_GALLERY);

        setContentView(R.layout.activity_box_image_crop);

        initView();
        // initControl();
        initListener();
    }
    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK) {
            try {
                Uri imgURI = data.getData();
                // ivProfile.setImageURI(imgURI);
                // filePath = getRealPathFromURI(imgURI);
                Bitmap bmp = MediaStore.Images.Media.getBitmap(getContentResolver(), imgURI);

                Log.e("Image Width", String.valueOf(bmp.getWidth()));
                Log.e("Image Height", String.valueOf(bmp.getHeight()));

                int targetWidth = bmp.getWidth();
                int targetHeight = bmp.getHeight();

                if(bmp.getWidth() > 4000 || bmp.getHeight() > 4000){
                    targetWidth = bmp.getWidth() / 5;
                    targetHeight = bmp.getHeight() / 5;
                }
                else if(bmp.getWidth() > 3000 || bmp.getHeight() > 3000){
                    targetWidth = bmp.getWidth() / 4;
                    targetHeight = bmp.getHeight() / 4;
                }
                else if(bmp.getWidth() > 1500 || bmp.getHeight() > 1500){
                    targetWidth = bmp.getWidth() / 3;
                    targetHeight = bmp.getHeight() / 3;
                }
                else if(bmp.getWidth() > 1000 || bmp.getHeight() > 1000){
                    targetWidth = bmp.getWidth() / 2;
                    targetHeight = bmp.getHeight() / 2;
                }

                // Bitmap croppedBitmap = Bitmap.createScaledBitmap(bmp, bmpWidth, bmpHeight, false);

                Matrix m = new Matrix();
                m.setRectToRect(new RectF(0, 0, bmp.getWidth(), bmp.getHeight()), new RectF(0, 0, targetWidth, targetHeight), Matrix.ScaleToFit.CENTER);
                Bitmap croppedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), m, true);
                // LinkBoxController.temporaryImage = bmp;
                // ivProfile.setImageBitmap(bmp);
                // ivProfile.getCroppedBitmap(bmp, 15);
                // ivProfile.setCropToPadding(true);
                cropImageView.setImageBitmap(croppedBitmap);
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
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView(){
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
    private void initListener(){
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
    //</editor-fold>
}
