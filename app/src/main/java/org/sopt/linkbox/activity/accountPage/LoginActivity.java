package org.sopt.linkbox.activity.accountPage;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.UsrType;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LoginActivity.class.getName() + " : ";
    
    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private Toolbar tToolbar = null;
    private EditText etEmail = null;
    private EditText etPass = null;
    private Button bLogin = null;
    private String emailHint = null;
    private String passwordHint = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initTouchListener();
        initListener();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView()  {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_login);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("로그인");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etEmail = (EditText) findViewById(R.id.ET_email_login);
        etPass = (EditText) findViewById(R.id.ET_password_login);
        bLogin = (Button) findViewById(R.id.B_login_login);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        etEmail.setTypeface(typeface);
        etPass.setTypeface(typeface);
    }
    private void initTouchListener() {
        bLogin.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bLogin.setBackgroundResource(R.drawable.custom_sign_rectangle_white);
                    bLogin.setTextColor(getResources().getColor(R.color.teal400));
                } else {
                    bLogin.setBackgroundResource(R.drawable.custom_sign_rectangle);
                    bLogin.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });

    }
    private void initListener() {
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrID = etEmail.getText().toString();
                String usrPassword = etPass.getText().toString();
                if (usrID.equals("") || usrPassword.equals("")) {
                    Toast.makeText(getApplicationContext(), "All Field must be filled!", Toast.LENGTH_LONG).show();
                    return;
                }

                InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mInputMethodManager.hideSoftInputFromWindow(etPass.getWindowToken(), 0);
                loginLoading(usrID, usrPassword);
            }
        });


    }
    //</editor-fold>

    //<editor-fold desc="Account Helper Methods" defaultstate="collapsed">
    private void loginLoading(String usremail, String pass) {
        Intent intent = new Intent(this, AccountLoadingActivity.class);
        intent.putExtra(AccountStrings.usrID, usremail);
        intent.putExtra(AccountStrings.usrPassword, pass);
        intent.putExtra(AccountStrings.usrType, UsrType.normal_user);
        startActivity(intent);
    }
    //</editor-fold>
}