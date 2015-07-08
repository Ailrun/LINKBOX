package org.sopt.linkbox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.*;

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
import org.sopt.linkbox.custom.adapters.HttpClient;


public class MainActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("start","start");

        Button loginButton=(Button)findViewById(R.id.LB_login_main);
        loginButton.setOnClickListener(this);
        AsyncHttpClient client=HttpClient.getInstance();
        PersistentCookieStore myCookieStore=new PersistentCookieStore(this);
        client.setCookieStore(myCookieStore);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
  //inflate the menu; this adds items to the action bar if it is present.
        setMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsSelected(MenuItem item) {
  //Handle action bar item clicks here. The action bar will
  //automatically handle clicks on the home/up button, so long
  //as you specify a parent activity in AndroidManifest.xml.
        int id=item.getItemId();
        if(id==R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        RequestParams params=new RequestParams();
        EditText id=(EditText)findViewById(R.id.AC_email_main);
        EditText pwd=(EditText)findViewById(R.id.TV_password_main);
        Log.i("Msg","Clicked Login Button id: "+id.getText()+" pwd: "+pwd.getText());
        params.put("id",id.getText().toString());
        params.put("pwd", pwd.getText().toString());
        HttpClient.get("", params, new AsyncHttpResponseHandler() {
            public void onSuccess(String response) {
                System.out.println(response){
                    TextView status = (TextView) findViewById(R.id.login_stauts);
                    status.setText(response toString());
                }
            }
            );

        }
    }