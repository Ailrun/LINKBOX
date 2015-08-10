package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Junyoung on 2015-08-08.
 */
public interface AlarmListInterface extends MainServerInterface {
    @GET(alarmList + "/AllList/{" + usrKey + "}")
    void allList(@Path(usrKey) int usrKey, Callback<MainServerData<List<AlarmListData>>> callback);
    @GET(alarmList + "/HiddenList/{" + usrKey + "}")
    void hiddenList(@Path(usrKey) int usrKey, Callback<MainServerData<List<AlarmListData>>> callback);
    @POST(alarmList + "/Hidden/{" + usrKey + "}/{" + alarmKey + "}")
    void hidden(@Path(usrKey) int usrKey, @Path(alarmKey) int alarmKey, Callback<MainServerData<Object>> callback);
}
