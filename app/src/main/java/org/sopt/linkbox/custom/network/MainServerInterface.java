package org.sopt.linkbox.custom.network;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface MainServerInterface {
    //        public static final String API_KEY = "???"; // Maybe Not needed
//String serverAPIEndPoint = "http://52.68.233.51:3000"; //Se-oong's Server
    String serverAPIEndPoint = "http://192.168.219.172:3000"; // Local Server IP

    String jsonHeader = "Content-Type: application/json";

    String usrList = "/usrList";

    String boxList = "/boxList";

    String urlList = "/urlList";
    String tag = urlList + "/Tag";
    String comment = urlList + "/Comment";

    String alarmList = "/alarmList";

    String usrKey = "usrKey";
    String boxKey = "boxKey";
    String alarmKey = "alarmKey";
    String urlKey = "urlKey";
    String newBoxKey = "newBoxKey";
    String startNum = "startNum";
    String urlNum = "urlNum";
    String deviceKey = "deviceKey";
}
