package org.sopt.linkbox.activity.accountPage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.UsrType;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + SignupActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private Toolbar tToolbar = null;
    private EditText etID = null;
    private EditText etName = null;
    private EditText etPassword = null;
    private EditText etPasswordCheck = null;
    private Button bSignup = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
        initTouchListener();
        initListener();
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_signup);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("회원가입");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Typeface typeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        etID = (EditText)findViewById(R.id.ET_id_signup);
        etName = (EditText)findViewById(R.id.ET_name_signup);
        etPassword = (EditText)findViewById(R.id.ET_password_signup);
        etPasswordCheck = (EditText)findViewById(R.id.ET_password_check_signup);
        bSignup = (Button)findViewById(R.id.B_signup_signup);

        etID.setTypeface(typeface);
        etName.setTypeface(typeface);
        etPassword.setTypeface(typeface);
        etPasswordCheck.setTypeface(typeface);
    }
    private void initTouchListener(){
        bSignup.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    bSignup.setBackgroundResource(R.drawable.custom_sign_rectangle_white);
                    bSignup.setTextColor(getResources().getColor(R.color.teal400));
                } else {
                    bSignup.setBackgroundResource(R.drawable.custom_sign_rectangle);
                    bSignup.setTextColor(getResources().getColor(R.color.real_white));
                }

                return false;
            }
        });
    }
    private void initListener() {
        bSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usrID = etID.getText().toString();
                String usrName = etName.getText().toString();
                String usrPassword = etPassword.getText().toString();
                if (usrPassword.equals(etPasswordCheck.getText().toString())) {
                    signupLoading(usrID, usrName, usrPassword);
                }
                else {
                    Toast.makeText(SignupActivity.this, "Password is incorrect!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    //</editor-fold>

    //<editor-fold desc="Account Helper Methods" defaultstate="collapsed">
    private void signupLoading(String usrID, String usrName, String usrPassword) {
        Intent intent = new Intent(this, AccountLoadingActivity.class);
        intent.putExtra(AccountStrings.usrID, usrID);
        intent.putExtra(AccountStrings.usrName, usrName);
        intent.putExtra(AccountStrings.usrPassword, usrPassword);
        intent.putExtra(AccountStrings.usrType, UsrType.new_user);
        startActivity(intent);
    }
    //</editor-fold>
}
