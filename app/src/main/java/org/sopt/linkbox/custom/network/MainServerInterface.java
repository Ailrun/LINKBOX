package org.sopt.linkbox.custom.network;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface MainServerInterface {
    //        public static final String API_KEY = "???"; // Maybe Not needed
    //String serverAPIEndPoint = "http://52.68.233.51:3000"; //Seoong's Server
    String serverAPIEndPoint = "http://54.69.181.225:3000"; // Junyoung's Server IP

    String jsonHeader = "Content-Type: application/json";

    String usrListPath = "/usrList";

    String boxListPath = "/boxList";

    String urlListPath = "/urlList";
    String tagPath = urlListPath + "/Tag";
    String commentPath = urlListPath + "/Comment";

    String alarmListPath = "/alarmList";

    String usrKeyPath = "usrKey";
    String boxKeyPath = "boxKey";
    String alarmKeyPath = "alarmKey";
    String urlKeyPath = "urlKey";
    String newBoxKeyPath = "newBoxKey";
    String startNumPath = "startNum";
    String urlNumPath = "urlNum";
    String deviceKeyPath = "deviceKey";
}
