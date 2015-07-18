package org.sopt.linkbox.custom.data.mainData;

/**
 * Created by Junyoung on 2015-07-08.
 */
public class UserData {
    public int usrid;
    public String usremail;
    public String usrname;
    public String usrprofile;
    public String pass;
    public boolean premium;
    @Override
    public String toString(){
        return "usrid="+usrid+", usremail="+usremail+", usrname="+usrname
                +", usrprofile="+usrprofile+", premium="+(premium?"true":"false");
    }
}
