package org.sopt.linkbox.activity.loginPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.LinkBoxActivity;
import org.sopt.linkbox.custom.data.mainData.UserData;

public class FacebookDataActivity extends Activity {
    CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_data);

        callbackManager = CallbackManager.Factory.create();
        Bundle parameter = new Bundle();
        parameter.putString("fields", "email, name, id, picture");

        GraphRequest graph = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                UserData userData = LinkBoxController.userData;

                userData.usrname = jsonObject.optString("name");
                userData.usremail = jsonObject.optString("email");
                try {
                    jsonObject = jsonObject.getJSONObject("picture");
                    jsonObject = jsonObject.getJSONObject("data");
                    userData.usrprofile = jsonObject.optString("picture");
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                userData.pass = "#$^(@#" + "Facebook" + "%#@$" + jsonObject.optString("id");
                startActivity(new Intent(getApplicationContext(), LinkBoxActivity.class));
                Log.e("jsonObject", userData.usrprofile);
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
