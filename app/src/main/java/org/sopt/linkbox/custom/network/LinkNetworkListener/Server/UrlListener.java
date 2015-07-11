package org.sopt.linkbox.custom.network.LinkNetworkListener.Server;

import org.sopt.linkbox.custom.data.LinkUrlListData;

import java.util.List;

import retrofit.RetrofitError;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface UrlListener {
    public void onUrlListSuccess(List<LinkUrlListData> linkUrlListDatas);
    public void onUrlListFail(RetrofitError error);
    public void onAddUrlSuccess(LinkUrlListData linkUrlListData);
    public void onAddUrlFail(RetrofitError error);
    public void onRemoveUrlSuccess(LinkUrlListData linkUrlListData);
    public void onRemoveUrlFail(RetrofitError error);
    public void onEditUrlSuccess(LinkUrlListData linkUrlListData);
    public void onEditUrlFail(RetrofitError error);
    public void onEditGoodSuccess(LinkUrlListData linkUrlListData);
    public void onEditGoodFail(RetrofitError error);
}
