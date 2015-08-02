package org.sopt.linkbox.activity.loginPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.sopt.linkbox.R;


public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + SignupActivity.class.getName() + " : ";

    private Toolbar tToolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initView();
    }

    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_signup);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
