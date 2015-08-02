package org.sopt.linkbox.custom.data.mainData;

import android.graphics.Bitmap;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class BoxListData {
    public int boxKey;
    public int boxIndex;
    public String boxName;
    public String boxThumbnail;
    public int boxUrlNum;
    public int isFavorite;
    @Override
    public String toString() {
        return "boxKey="+ boxKey +", boxIndex="+ boxIndex +", boxName="+ boxName;
    }
}
