package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Junyoung on 2015-08-08.
 */
public interface BoxListInterface extends MainServerInterface {
    @GET(boxList + "/List/{" + usrKey + "}")
    void list(@Path(usrKey) int usrKey, Callback<MainServerData<List<BoxListData>>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Add/{" + usrKey + "}")
    void add(@Path(usrKey) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Remove/{" + usrKey + "}")
    void remove(@Path(usrKey) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Edit/{" + usrKey + "}")
    void edit(@Path(usrKey) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Favorite/{" + usrKey + "}")
    void favorite(@Path(usrKey) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Invite/{" + usrKey + "}/{" + boxKey + "}")
    void invite(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body TwoString twoString, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Accept/{" + alarmKey + "}")
    void accept(@Path(alarmKey) int alarmKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Decline/{" + alarmKey + "}")
    void decline(@Path(alarmKey) int alarmKey, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(boxList + "/Editor/{" + usrKey + "}")
    void editorList(@Path(usrKey) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<List<UsrListData>>> callback);
}
