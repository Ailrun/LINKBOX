package org.sopt.linkbox.custom.data.mainData;

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
    public GoodData goodData;
    @Override
    public String toString(){
        return "urlKey="+ urlKey +", url="+ url +", urlTitle="+ urlTitle
                +", urlWriterUsrName="+ urlWriterUsrName +", urlDate="+ urlDate +", urlThumbnail"+ urlThumbnail
                + ((goodData == null)?"": goodData.toString());
    }
}
