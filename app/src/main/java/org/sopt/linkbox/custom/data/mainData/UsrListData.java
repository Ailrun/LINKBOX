package org.sopt.linkbox.custom.data.mainData;

/**
 * Created by Junyoung on 2015-07-08.
 */
public class UsrListData {
    public int usrKey;
    public String usrID;
    public String usrName;
    public String usrProfile;
    public String usrPassword;
    public int usrType;
    public int premium;
    @Override
    public String toString(){
        return "usrKey="+ usrKey +", usrID="+ usrID +", usrName="+ usrName
                +", usrProfile="+ usrProfile +", premium="+(premium!=0?"true":"false");
    }
}
