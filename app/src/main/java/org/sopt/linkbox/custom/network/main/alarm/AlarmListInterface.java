package org.sopt.linkbox.custom.network.main.alarm;

import org.sopt.linkbox.custom.data.mainData.AlarmListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.network.main.MainServerInterface;

import java.util.List;
import java.util.Objects;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Junyoung on 2015-08-08.
 *
 */
public interface AlarmListInterface {
    @GET(MainServerInterface.alarmListPath + "/AllList/{" + MainServerInterface.usrKeyPath + "}")
    void allList(@Path(MainServerInterface.usrKeyPath) int usrKey, Callback<MainServerData<List<AlarmListData>>> callback);
    @GET(MainServerInterface.alarmListPath + "/HiddenList/{" + MainServerInterface.usrKeyPath + "}")
    void hiddenList(@Path(MainServerInterface.usrKeyPath) int usrKey, Callback<MainServerData<List<AlarmListData>>> callback);
    @POST(MainServerInterface.alarmListPath + "/Hidden/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.alarmKeyPath + "}")
    void hidden(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.alarmKeyPath) int alarmKey, Callback<MainServerData<Object>> callback);
    @POST(MainServerInterface.alarmListPath + "/Read/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.alarmKeyPath + "}")
    void read(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.alarmKeyPath) int alarmKey, Callback<MainServerData<Object>> callback);
}
