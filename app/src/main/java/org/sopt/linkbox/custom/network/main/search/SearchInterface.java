package org.sopt.linkbox.custom.network.main.search;

import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
import org.sopt.linkbox.custom.data.tempData.SearchBody;
import org.sopt.linkbox.custom.network.main.MainServerInterface;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by sy on 2015-09-19.
 */
public interface SearchInterface {
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.searchPath + "/AllSearch/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}/{" + MainServerInterface.searchType + "}")
    void allSearch(@Path(MainServerInterface.usrKeyPath) int usrkey, @Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath)int urlNum, @Path(MainServerInterface.searchType)int searchType, @Body SearchBody searchbody, Callback<MainServerData<List<UrlListData>>> callback);

    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.searchPath + "/AllSearch/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}/{" + MainServerInterface.searchType + "}")
    void boxSearch(@Path(MainServerInterface.usrKeyPath) int usrkey, @Path(MainServerInterface.boxKeyPath) int boxKey ,@Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath)int urlNum, @Path(MainServerInterface.searchType)int searchType, @Body SearchBody searchbody, Callback<MainServerData<List<UrlListData>>> callback);

}
