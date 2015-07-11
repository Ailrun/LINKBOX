package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.custom.data.LinkUserData;
import org.sopt.linkbox.custom.network.LinkNetwork;

public class LoginDataActivity extends Activity {
    CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_data);

        callbackManager = CallbackManager.Factory.create();
        Bundle parameter = new Bundle();
        parameter.putString("fields", "email, name, id, picture");

        GraphRequest graph = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                LinkUserData linkUserData = LinkBoxController.linkUserData;

                linkUserData.usrname = jsonObject.optString("name");
                linkUserData.usremail = jsonObject.optString("email");
                try {
                    jsonObject = jsonObject.getJSONObject("picture");
                    jsonObject = jsonObject.getJSONObject("data");
                    linkUserData.usrprofile = jsonObject.optString("picture");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                linkUserData.pass = "#$^(@#" + "Facebook" + "%#@$" + jsonObject.optString("id");
                LinkNetwork.Server.postSignupToServerSync();
                LinkNetwork.Server.postLoginToServerAsync(LoginDataActivity.this);
                startActivity(new Intent(getApplicationContext(), LinkBoxActivity.class));
                Log.e("jsonObject", linkUserData.usrprofile);
                Log.e("jsonObject", jsonObject.toString());
            }
        });
        graph.setParameters(parameter);
        graph.executeAsync();
    }
    protected  void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken() == null){
            finish();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
