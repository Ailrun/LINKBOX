package org.sopt.linkbox.custom.data.mainData;

/**
 * Created by Junyoung on 2015-07-06.
 *
 */
public class UrlListData {
    public int urlid;
    public String address;
    public String urlname;
    public String urlwriter;
    public String urldate;
    public String urlthumb;
    public GoodData gooddata;
    @Override
    public String toString(){
        return "urlid="+urlid+", address="+address+", urlname="+urlname
                +", urlwriter="+urlwriter+", urldate="+urldate+", urlthumb"+urlthumb
                + ((gooddata == null)?"":gooddata.toString());
    }
}
