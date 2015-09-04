package org.sopt.linkbox.activity.accountPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONObject;
import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.UsrType;

public class FacebookDataActivity extends Activity {
    private static final String TAG = "TEST/" + FacebookDataActivity.class.getName() + " : ";

    private CallbackManager callbackManager = null;

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
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
            Log.e("jsonObject", LinkBoxController.usrListData.usrProfile);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //</editor-fold>

    //<editor-fold desc="Graph Classes" defaultstate="collapsed">
    private class GraphCallback implements GraphRequest.GraphJSONObjectCallback {
        private final String TAG = "TEST/" + GraphCallback.class.getName() + " : ";
        @Override
        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
            if (jsonObject != null) {
                String usrProfile = jsonObject.optJSONObject("picture").optJSONObject("data").optString("url");
                String usrPassword = "#$^(@#" + "Facebook" + "%#@$" + jsonObject.optString("id");
                Log.d(TAG, jsonObject.toString());
                facebookLoading(jsonObject.optString("email"), jsonObject.optString("name"), usrProfile, usrPassword);
            }
            else {
                Log.d(TAG, "jsonObject is NULL!!!!");
                Toast.makeText(FacebookDataActivity.this, "Facebook과의 연결에 문제가 있습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    //</editor-fold>

    //<editor-fold desc="Account Helper Methods" defaultstate="collapsed">
    private void facebookLoading(String usrID, String usrName, String usrProfile, String usrPassword) {
        Intent intent = new Intent(this, AccountLoadingActivity.class);
        intent.putExtra(AccountStrings.usrID, usrID);
        intent.putExtra(AccountStrings.usrName, usrName);
        intent.putExtra(AccountStrings.usrProfile, usrProfile);
        intent.putExtra(AccountStrings.usrPassword, usrPassword);
        intent.putExtra(AccountStrings.usrType, UsrType.facebook_user);
        startActivity(intent);
        finish();
    }
    //</editor-fold>
}
