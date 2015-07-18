package org.sopt.linkbox.activity.loginPage;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TabHost;

import org.sopt.linkbox.R;
import org.sopt.linkbox.service.LinkHeadService;


public class MainActivity extends TabActivity {
    private TabHost tabHost;
    private Toolbar tToolbar = null;
    private ImageView ivTitle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stopService(new Intent(getApplicationContext(), LinkHeadService.class));

        setContentView(R.layout.activity_main);
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
    public void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
    }
}
