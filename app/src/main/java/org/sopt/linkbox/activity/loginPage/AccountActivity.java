package org.sopt.linkbox.activity.loginPage;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TabHost;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.helper.SessionSaver;


public class AccountActivity extends TabActivity {
    private TabHost tabHost;
    private Toolbar tToolbar = null;
    private ImageView ivTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);
        ivTitle = (ImageView) findViewById(R.id.IV_image_main);
        ivTitle.bringToFront();

        tabHost = getTabHost();
        TabHost.TabSpec spec = null;
        Intent intent = null;

        intent = new Intent(this, LoginActivity.class);
        spec = tabHost.newTabSpec("Login").setIndicator("Login")
                .setContent(intent);
        tabHost.addTab(spec);

        intent = new Intent(this, SignupActivity.class);
        spec = tabHost.newTabSpec("Sign Up").setIndicator("Sign Up")
                .setContent(intent);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        SessionSaver.saveSession(this);
    }
}
