package org.sopt.linkbox.custom.data.mainData.url;

import java.sql.Date;
import java.util.List;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class UrlListData implements Cloneable {
    public int urlKey;
    public String url;
    public String urlTitle;
    public int urlWriterUsrKey;
    public String urlWriterUsrName;
    public String urlDate;
    public String urlThumbnail;
    public int liked;
    public int hidden;
    public int readLater;
    public int likedNum;
    public List<TagListData> urlTags;
    @Override
    public String toString(){
        return "urlKey="+ urlKey +", url="+ url +", urlTitle="+ urlTitle + "\n"
                +"urlWriterUsrName="+ urlWriterUsrName +", urlDate="+ urlDate +", urlThumbnail"+ urlThumbnail + "\n"
                + (liked ==0?"False":"True") + ", likedNum=" + likedNum;
    }
    @Override
    public UrlListData clone() {
        try {
            return (UrlListData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return new UrlListData();
        }
    }
    public String getTag(int i)
    {
        return urlTags.get(i).tag;
    }
}
