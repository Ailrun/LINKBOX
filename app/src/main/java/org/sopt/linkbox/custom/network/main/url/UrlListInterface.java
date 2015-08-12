package org.sopt.linkbox.custom.network.main.url;

import org.sopt.linkbox.custom.data.mainData.url.CommentListData;
import org.sopt.linkbox.custom.data.mainData.url.TagListData;
import org.sopt.linkbox.custom.data.mainData.url.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;
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
public interface UrlListInterface {
    @GET(MainServerInterface.urlListPath + "/AllList/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}")
    void allList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(MainServerInterface.urlListPath + "/FavoriteList/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}")
    void favoriteList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(MainServerInterface.urlListPath + "/HiddenList/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}")
    void hiddenList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(MainServerInterface.urlListPath + "/BoxList/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.startNumPath + "}/{" + MainServerInterface.urlNumPath + "}")
    void boxList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.startNumPath) int startNum, @Path(MainServerInterface.urlNumPath) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Add/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void add(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Remove/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void remove(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Edit/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void edit(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Hidden/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void hidden(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Share/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.newBoxKeyPath + "}")
    void share(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.newBoxKeyPath) int newBoxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.urlListPath + "/Like/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}")
    void like(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);

    @GET(MainServerInterface.tagPath + "/List/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void tagList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, Callback<MainServerData<List<TagListData>>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.tagPath + "/Add/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void tagAdd(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.tagPath + "/Remove/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void tagRemove(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);

    @GET(MainServerInterface.commentPath + "/List/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void commentList(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, Callback<MainServerData<List<CommentListData>>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.commentPath + "/Add/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void commentAdd(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
    @Headers(MainServerInterface.jsonHeader)
    @POST(MainServerInterface.commentPath + "/Remove/{" + MainServerInterface.usrKeyPath + "}/{" + MainServerInterface.boxKeyPath + "}/{" + MainServerInterface.urlKeyPath + "}")
    void commentRemove(@Path(MainServerInterface.usrKeyPath) int usrKey, @Path(MainServerInterface.boxKeyPath) int boxKey, @Path(MainServerInterface.urlKeyPath) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
}
