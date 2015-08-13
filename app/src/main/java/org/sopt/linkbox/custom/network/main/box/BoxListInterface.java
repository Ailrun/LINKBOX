package org.sopt.linkbox.custom.network.main.box;

import org.sopt.linkbox.custom.data.mainData.BoxListData;
import org.sopt.linkbox.custom.data.mainData.UsrListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.TwoString;
import org.sopt.linkbox.custom.network.main.MainServerInterface;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Junyoung on 2015-08-08.
 *
 */
public interface BoxListInterface {
    @GET(MainServerInterface.boxListPath + "/List/{" + MainServerInterface.usrKeyPath + "}")
    void list(@Path(MainServerInterface.usrKeyPath) int usrKey, Callback<MainServerData<List<BoxListData>>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Add/{" + MainServerInterface.usrKeyPath + "}")
    void add(@Path(MainServerInterface.usrKeyPath) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<BoxListData>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Remove/{" + MainServerInterface.usrKeyPath + "}")
    void remove(@Path(MainServerInterface.usrKeyPath) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Edit/{" + MainServerInterface.usrKeyPath + "}")
    void edit(@Path(MainServerInterface.usrKeyPath) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Favorite/{" + MainServerInterface.usrKeyPath + "}")
    void favorite(@Path(MainServerInterface.usrKeyPath) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Invite/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void invite(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body TwoString twoString, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Accept/{" + MainServerInterface.alarmKeyPath + "}")
    void accept(@Path(MainServerInterface.alarmKeyPath) int alarmKey, @Body BoxListData boxListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Decline/{" + MainServerInterface.alarmKeyPath + "}")
    void decline(@Path(MainServerInterface.alarmKeyPath) int alarmKey, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.boxListPath + "/Editor/{" + MainServerInterface.usrKeyPath + "}")
    void editorList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Body BoxListData boxListData, Callback<MainServerData<List<UsrListData>>> callback);
}
