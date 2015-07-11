package org.sopt.linkbox.custom.network.LinkNetworkListener.Server;

import org.sopt.linkbox.custom.data.LinkUserData;

import retrofit.RetrofitError;

/**
* Created by Junyoung on 2015-07-11.
*/
public interface AuthorizationListener {
    public void onLoginSuccess(LinkUserData linkUserData);
    public void onLoginFail(RetrofitError error);
    public void onSignupSuccess(LinkUserData linkUserData);
    public void onSignupFail(RetrofitError error);
}
