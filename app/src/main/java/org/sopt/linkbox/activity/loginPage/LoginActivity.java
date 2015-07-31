package org.sopt.linkbox.activity.loginPage;

import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.LoginLoadingActivity;
import org.sopt.linkbox.constant.LoginStrings;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LoginActivity.class.getName() + " : ";

    private Toolbar tToolbar = null;
    private EditText etEmail = null;
    private EditText etPass = null;
    private ImageButton ibLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }

    private void initView()  {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_login);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        etEmail = (EditText) findViewById(R.id.ET_email_login);
        etPass = (EditText) findViewById(R.id.ET_password_login);
        ibLogin = (ImageButton) findViewById(R.id.IB_login_login);
    }
    private void initListener() {
        ibLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usremail = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                if (usremail.equals("") || pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "All Field must be filled!", Toast.LENGTH_LONG).show();
                    return;
                }
                loginLoading(usremail, pass);
            }
        });
    }

    private void loginLoading(String usremail, String pass) {
        Intent intent = new Intent(this, LoginLoadingActivity.class);
        intent.putExtra(LoginStrings.usrID, usremail);
        intent.putExtra(LoginStrings.usrPassword, pass);
        startActivity(intent);
        finish();
    }
}