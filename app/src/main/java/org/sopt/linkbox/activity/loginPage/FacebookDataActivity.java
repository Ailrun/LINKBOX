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
import org.sopt.linkbox.activity.loadingPage.LoginLoadingActivity;
import org.sopt.linkbox.constant.LoginStrings;

public class FacebookDataActivity extends Activity {
    private static final String TAG = "TEST/" + FacebookDataActivity.class.getName() + " : ";

    private CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_facebook_data);

        callbackManager = CallbackManager.Factory.create();
        Bundle parameter = new Bundle();
        parameter.putString("fields", "name,id,picture{url},email");

        GraphRequest graph = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphCallback());
        graph.setParameters(parameter);
        Log.d(TAG, graph.toString());
        Log.d(TAG, graph.getVersion());
        graph.executeAsync();
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken() == null){
            finish();
            Log.e("jsonObject", LinkBoxController.userData.usrprofile);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private class GraphCallback implements GraphRequest.GraphJSONObjectCallback {
        private final String TAG = "TEST/" + GraphCallback.class.getName() + " : ";
        @Override
        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
            String usrprofile = new String();
            String pass = new String();

            try {
                usrprofile = jsonObject.getJSONObject("picture").getJSONObject("data").optString("url");
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            pass = "#$^(@#" + "Facebook" + "%#@$" + jsonObject.optString("id");
            Log.d(TAG, jsonObject.toString());
            Intent intent = new Intent(FacebookDataActivity.this, LoginLoadingActivity.class);
            intent.putExtra(LoginStrings.usremail, jsonObject.optString("email"));
            intent.putExtra(LoginStrings.usrname, jsonObject.optString("name"));
            intent.putExtra(LoginStrings.usrprofile, usrprofile);
            intent.putExtra(LoginStrings.pass, pass);
            startActivity(intent);
            finish();
        }
    }
}
