package org.sopt.linkbox.custom.data.mainData;

/**
 * Created by sy on 2015-08-12.
 */
public class InviteBoxData {

        public int boxKey;
        public int boxIndex;
        public String boxName;
        public String boxThumbnail;
        public String boxDate;
        public int boxUrlNum;
        public int isFavorite;
        @Override
        public String toString() {
            return "boxKey="+ boxKey +", boxIndex="+ boxIndex +", boxName="+ boxName;
        }
        @Override
        public BoxListData clone() {
            try {
                return (BoxListData) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return new BoxListData();
            }
        }

}
