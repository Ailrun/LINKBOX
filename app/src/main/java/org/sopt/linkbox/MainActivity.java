package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;


// 로그인 액티비티 구현
class LoginActivity extends Activity {

    CallbackManager callbackManager;
    LoginButton loginButton;

}

public class MainActivity extends Activity {

    CallbackManager callbackManager;
    LoginButton button_login;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        callbackManager = CallbackManager.Factory.create();

        loginButton=(LoginButton) findViewById(R.id.button_login);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginButton.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
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
        button_login = (LoginButton) findViewById(R.id.button_login);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        Bundle parameter=new Bundle();
        parameter.putString("fields","id,password");

        GraphRequest request= GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                TextView textView_id,textView_name,textView_password;


                textView_id=(TextView) findViewById(R.id.textView_id);
                textView_password=(TextView) findViewById(R.id.textView_password);


                textView_id.setText(jsonObject.optString("id"));
                textView_password.setText(jsonObject.optString("password"));

            }
        });
        request.setParameters(parameter);
        request.executeAsync();


    }

    protected void onResume(){
        super.onResume();
        if(AccessToken.getCurrentAccessToken()!=null){
            loginButton.setVisibility(View.INVISIBLE);
            Intent intent=new Intent(getApplicationContext(), MainActivity.class);
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
