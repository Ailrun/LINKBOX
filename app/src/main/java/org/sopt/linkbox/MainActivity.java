package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONObject;


// 로그인 액티비티 구현


public class MainActivity extends Activity {

    CallbackManager callbackManager;
    Button button_login;

=======

public class MainActivity extends Activity {

>>>>>>> f3e7bc6f3fa40bd65d462b32ef80da6d50a8ee54
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
<<<<<<< HEAD

        callbackManager = CallbackManager.Factory.create();

        button_login = (Button) findViewById(R.id.button_login);
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
        if(AccessToken.getCurrentAccessToken()==null){
            Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    protected void onActivityResult(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

=======
>>>>>>> f3e7bc6f3fa40bd65d462b32ef80da6d50a8ee54
    }
}
