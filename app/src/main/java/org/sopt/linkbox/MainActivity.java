package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


public class MainActivity extends Activity {

    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, LinkBoxActivity.class));

        callbackManager = CallbackManager.Factory.create();
        loginButton=(LoginButton) findViewById(R.id.LB_login_main);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext(), LoginDataActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onError(FacebookException e) {
            }
        });
        Bundle parameter=new Bundle();
        parameter.putString("fields","id,password");

        GraphRequest request= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                TextView tvId, tvPassword;

                tvId=(TextView) findViewById(R.id.TV_id_main);
                tvPassword=(TextView) findViewById(R.id.TV_password_main);

                tvId.setText(jsonObject.optString("id"));
                tvPassword.setText(jsonObject.optString("password"));
            }
        });
        request.setParameters(parameter);
        request.executeAsync();
    }

    protected void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken()!=null){
            loginButton.setVisibility(View.INVISIBLE);
            Intent intent=new Intent(getApplicationContext(), LoginDataActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            loginButton.setVisibility(View.VISIBLE);
        }
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
