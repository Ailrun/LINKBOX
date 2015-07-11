package org.sopt.linkbox.custom.network.LinkNetworkListener.Server;

import org.sopt.linkbox.custom.data.LinkBoxListData;

import java.util.List;

import retrofit.RetrofitError;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface BoxListener {
    public void onBoxListSuccess(List<LinkBoxListData> linkBoxListDatas);
    public void onBoxListFail(RetrofitError error);
    public void onAddBoxSuccess(LinkBoxListData linkBoxListData);
    public void onAddBoxFail(RetrofitError error);
    public void onRemoveBoxSuccess();
    public void onRemoveBoxFail(RetrofitError error);
    public void onEditBoxSuccess(LinkBoxListData linkBoxListData);
    public void onEditBoxFail(RetrofitError error);
}
