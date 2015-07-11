package org.sopt.linkbox.custom.network.LinkNetworkListener.Server;

import org.sopt.linkbox.custom.data.LinkUserData;

import java.util.List;

import retrofit.RetrofitError;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface EditorListener {
    public void onUsrListSuccess(List<LinkUserData> linkUserDatas);
    public void onUsrListFail(RetrofitError error);
    public void onAddUsrSuccess(LinkUserData linkUserData);
    public void onAddUsrSuccess(RetrofitError error);
}
