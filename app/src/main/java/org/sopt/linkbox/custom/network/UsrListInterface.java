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
 */
public interface UsrListInterface extends MainServerInterface {
    @Headers(jsonHeader)
    @POST(usrList + "/Login/{" + deviceKey + "}")
    void login(@Path(deviceKey) String imei, @Body UsrListData usrListData, Callback<MainServerData<UsrListData>> callback);
    @Headers(jsonHeader)
    @POST(usrList + "/Signup/{" + deviceKey + "}")
    void signup(@Path(deviceKey) String imei, @Body UsrListData usrListData, Callback<MainServerData<UsrListData>> callback);
    @Headers(jsonHeader)
    @POST(usrList + "/Profile")
    void profile(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(usrList + "/PassEdit")
    void passEdit(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(usrList + "/Logout/{" + deviceKey + "}")
    void logout(@Path(deviceKey) String imei, @Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(usrList + "/Signdown")
    void signdown(@Body UsrListData usrListData, Callback<MainServerData<Object>> callback);
}
