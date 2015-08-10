package org.sopt.linkbox.custom.network;

import org.sopt.linkbox.custom.data.mainData.CommentListData;
import org.sopt.linkbox.custom.data.mainData.TagListData;
import org.sopt.linkbox.custom.data.mainData.UrlListData;
import org.sopt.linkbox.custom.data.networkData.MainServerData;

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
public interface UrlListInterface extends MainServerInterface {
    @GET(urlList + "/AllList/{" + usrKey + "}/{" + startNum + "}/{" + urlNum + "}")
    void allList(@Path(usrKey) int usrKey, @Path(startNum) int startNum, @Path(urlNum) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(urlList + "/FavoriteList/{" + usrKey + "}/{" + startNum + "}/{" + urlNum + "}")
    void favoriteList(@Path(usrKey) int usrKey, @Path(startNum) int startNum, @Path(urlNum) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(urlList + "/HiddenList/{" + usrKey + "}/{" + startNum + "}/{" + urlNum + "}")
    void hiddenList(@Path(usrKey) int usrKey, @Path(startNum) int startNum, @Path(urlNum) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @GET(urlList + "/BoxList/{" + usrKey + "}/{" + boxKey + "}/{" + startNum + "}/{" + urlNum + "}")
    void boxList(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(startNum) int startNum, @Path(urlNum) int urlNum, Callback<MainServerData<List<UrlListData>>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Add/{" + usrKey + "}/{" + boxKey + "}")
    void add(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Remove/{" + usrKey + "}/{" + boxKey + "}")
    void remove(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Edit/{" + usrKey + "}/{" + boxKey + "}")
    void edit(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Hidden/{" + usrKey + "}/{" + boxKey + "}")
    void hidden(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Share/{" + usrKey + "}/{" + boxKey + "}/{" + newBoxKey + "}")
    void share(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(newBoxKey) int newBoxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(urlList + "/Like/{" + usrKey + "}/{" + boxKey + "}")
    void like(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Body UrlListData urlListData, Callback<MainServerData<Object>> callback);

    @GET(tag + "/List/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void tagList(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, Callback<MainServerData<List<TagListData>>> callback);
    @Headers(jsonHeader)
    @POST(tag + "/Add/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void tagAdd(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(tag + "/Remove/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void tagRemove(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);

    @GET(comment + "/List/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void commentList(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, Callback<MainServerData<List<CommentListData>>> callback);
    @Headers(jsonHeader)
    @POST(comment + "/Add/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void commentAdd(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
    @Headers(jsonHeader)
    @POST(comment + "/Remove/{" + usrKey + "}/{" + boxKey + "}/{" + urlKey + "}")
    void commentRemove(@Path(usrKey) int usrKey, @Path(boxKey) int boxKey, @Path(urlKey) int urlKey, @Body TagListData tagListData, Callback<MainServerData<Object>> callback);
}
