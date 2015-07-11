package org.sopt.linkbox;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

import org.sopt.linkbox.service.LinkHeadService;


public class MainActivity extends TabActivity {
    TabHost tabHost;
    Toolbar tToolbar = null;
    ImageView ivTitle = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stopService(new Intent(getApplicationContext(), LinkHeadService.class));

        setContentView(R.layout.activity_main);
        ivTitle = (ImageView) findViewById(R.id.IV_main_image);
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

        //LayoutInflater.from(this).inflate(R.layout.activity_main, tabHost.getTabContentView(), false);
        //setContentView(R.layout.activity_main);
        //tabHost = getTabHost();
        //tabHost = (FragmentTabHost)findViewById(R.id.tabHost);
        //TabHost tabHost = (TabHost)findViewById(R.id.tabHost);

        //tabHost.setup();

        //tabHost.addTab(tabHost.newTabSpec("loginTab").setIndicator("로그인"), LoginFragment.class ,null);
        //tabHost.addTab(tabHost.newTabSpec("signupTab").setIndicator("회원가입"), SignupFragment.class ,null);

/**
        TabHost.TabSpec tabSpec=tabHost.newTabSpec("loginTab").setIndicator("로그인").setContent(R.id.loginTab);
        tabHost.addTab(tabSpec);
        tabSpec=tabHost.newTabSpec("signupTab").setIndicator("회원가입").setContent(R.id.signupTab);
        tabHost.addTab(tabSpec);*/
        /**
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("loginTab").setIndicator("login").setContent(R.id.tabcontent);
        tabHost.addTab(tabSpec);*/
        /**
         * TabHost.TabSpec tabSpec=tabHost.newTabSpec("loginTab").setIndicator("로그인").setContent(new Intent(this, LoginFragment.class));
        tabHost.addTab(tabSpec);
        tabSpec=tabHost.newTabSpec("signupTab").setIndicator("회원가입").setContent(new Intent(this, SignupFragment.class));
        tabHost.addTab(tabSpec);
         */



        //TabHost.TabSpec tabSpec=tabHost.newTabSpec("loginTab").setIndicator("로그인").setContent(new Intent(this, LoginActivity.class));
        //tabHost.addTab(tabSpec);
        //tabSpec=tabHost.newTabSpec("signupTab").setIndicator("회원가입").setContent(new Intent(this, SignupActivity.class));
        //tabHost.addTab(tabSpec);



/**
         Intent intent_signup = new Intent(this, SignupActivity.class);
        tabSpec=tabHost.newTabSpec("signupTab").setIndicator("회원가입").setContent(intent_signup);
        tabHost.addTab(tabSpec);
*/

    }
    @Override
    public void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
    }
}
