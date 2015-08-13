package org.sopt.linkbox.activity.accountPage;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loadingPage.AccountLoadingActivity;
import org.sopt.linkbox.constant.AccountStrings;
import org.sopt.linkbox.constant.UsrType;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LoginActivity.class.getName() + " : ";

    private Toolbar tToolbar = null;
    private EditText etEmail = null;
    private EditText etPass = null;
    private Button bLogin = null;
    private String emailHint = null;
    private String passwordHint = null;

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
        bLogin = (Button) findViewById(R.id.B_login_login);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        etEmail.setTypeface(typeface);
        etPass.setTypeface(typeface);
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
                loginLoading(usrID, usrPassword);
            }
        });
    }

    private void loginLoading(String usremail, String pass) {
        Intent intent = new Intent(this, AccountLoadingActivity.class);
        intent.putExtra(AccountStrings.usrID, usremail);
        intent.putExtra(AccountStrings.usrPassword, pass);
        intent.putExtra(AccountStrings.usrType, UsrType.normal_user);
        startActivity(intent);
        finish();
    }
}