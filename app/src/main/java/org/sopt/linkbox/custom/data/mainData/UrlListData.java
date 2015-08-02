package org.sopt.linkbox.custom.data.mainData;

import java.util.List;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class UrlListData {
    public int urlKey;
    public String url;
    public String urlTitle;
    public String urlWriterUsrName;
    public String urlDate;
    public String urlThumbnail;
    public int isGood;
    public int goodNum;
    public List<String> urlTags;
    @Override
    public String toString(){
        return "urlKey="+ urlKey +", url="+ url +", urlTitle="+ urlTitle
                +", urlWriterUsrName="+ urlWriterUsrName +", urlDate="+ urlDate +", urlThumbnail"+ urlThumbnail
                + (isGood==0?"True":"False") + "goodNum=" + goodNum;
    }
    public String getTag(int i)
    {
        return urlTags.get(i);
    }
}
