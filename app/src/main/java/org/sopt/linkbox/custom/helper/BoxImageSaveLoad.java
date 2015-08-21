package org.sopt.linkbox.custom.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.custom.data.mainData.BoxListData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by MinGu on 2015-08-13.
 */
public class BoxImageSaveLoad {

    private static Context context;

    public BoxImageSaveLoad(Context receivedContext){
        BoxImageSaveLoad.context = receivedContext;
    }

    public String saveProfileImage(Bitmap bitmapImage, int position){
        ContextWrapper cw = new ContextWrapper(context);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("boxImageDir", Context.MODE_PRIVATE);
        // Create imageDir
        BoxListData boxListData = LinkBoxController.boxListSource.get(position);
        int boxKey = boxListData.boxKey;
        File boxPath = new File(directory, boxKey + ".png");
        Log.e("BoxImage Save Status : ", boxPath.toString());

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(boxPath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }

    public Bitmap loadProfileImage(int position) {
        Bitmap temporaryImage = null;
        try {
            BoxListData boxListData = LinkBoxController.boxListSource.get(position);
            Log.e("boxListSource position", String.valueOf(LinkBoxController.boxListSource.get(position)));
            int boxKey = boxListData.boxKey;

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("boxImageDir", Context.MODE_PRIVATE);
            File file = new File(directory, boxKey + ".png");
            temporaryImage = BitmapFactory.decodeStream(new FileInputStream(file));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return temporaryImage;
    }
}
