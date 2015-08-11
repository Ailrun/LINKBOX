package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Junyoung on 2015-08-08.
 *
 */
public interface UsrListInterface {
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/Login/{" + MainServerInterface.deviceKeyPath + "}")
    void login(@Path(MainServerInterface.deviceKeyPath) String imei, @Body UsrListData usrListData, Callback<MainServerData<UsrListData>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/Signup/{" + MainServerInterface.deviceKeyPath + "}")
    void signup(@Path(MainServerInterface.deviceKeyPath) String imei, @Body UsrListData usrListData, Callback<MainServerData<UsrListData>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/Profile")
    void profile(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/PassEdit")
    void passEdit(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/Logout/{" + MainServerInterface.deviceKeyPath + "}")
    void logout(@Path(MainServerInterface.deviceKeyPath) String imei, @Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.usrListPath + "/Signdown")
    void signdown(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
}
