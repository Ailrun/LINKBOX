package org.sopt.linkbox.activity.mainPage.urlListingPage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
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
 * Created by MinGu on 2015-08-11.
 *
 */
public class PhotoCropActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + PhotoCropActivity.class.getName() + " : ";

    protected final int SELECT_GALLERY = 1;

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

        setContentView(R.layout.activity_photo_crop);

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
                /*
                if(bmp.getHeight() > 2000 || bmp.getWidth() > 2000){
                    bmp = resizeBitmap(bmp, 500.0f, 500.0f);
                }
                Log.e("Bitmap Size WIDTH", String.valueOf(bmp.getWidth()));
                Log.e("Bitmap Size HEIGHT", String.valueOf(bmp.getHeight()));
                */
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
            if(LinkBoxController.userImage != null){
                cropImageView.setImageBitmap(LinkBoxController.userImage);
            }
            else{
                finish();
            }
        }
    }
    //</editor-fold>

    /*
    public Bitmap resizeBitmap(Bitmap bmp, float newWidth, float newHeight){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bmp, 0, 0, width, height, matrix, false);
        bmp.recycle();
        return resizedBitmap;
    }
    */

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
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
                LinkBoxController.userImage = cropImageView.getCroppedImage();
                // String saveStatus = .saveProfileImage(LinkBoxController.userImage);
                // Log.d("Save Status : ", saveStatus);
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
