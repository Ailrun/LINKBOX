package org.sopt.linkbox.custom.helper;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.UsrListData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by MinGu on 2015-08-12.
 * KingWangJJang
 *
 */
public class ImageSaveLoad extends Application {

    private static Context context = null;

    public void onCreate(){
        super.onCreate();
        this.context = this.getApplicationContext();

    }
    public static String saveProfileImage(Bitmap bitmapImage){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        UsrListData userData = LinkBoxController.usrListData;
        String userId = userData.usrID;
        File mypath=new File(directory, userId + ".jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public static Bitmap loadProfileImage() {
        Bitmap temporaryImage = null;
        try {
            UsrListData userData = LinkBoxController.usrListData;
            String userId = userData.usrID;

            ContextWrapper cw = new ContextWrapper(context);
            if (cw != null && context != null) {
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                File file = new File(directory, userId + ".jpg");
                Log.e("", file.toString());
                temporaryImage = BitmapFactory.decodeStream(new FileInputStream(file));
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return temporaryImage;
    }
}
