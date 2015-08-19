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
    public int good;
    public int hidden;
    public int readLater;
    public int goodNum;
    public List<TagListData> urlTags;
    @Override
    public String toString(){
        return "urlKey="+ urlKey +", url="+ url +", urlTitle="+ urlTitle
                +", urlWriterUsrName="+ urlWriterUsrName +", urlDate="+ urlDate +", urlThumbnail"+ urlThumbnail
                + (good==0?"True":"False") + "goodNum=" + goodNum;
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
