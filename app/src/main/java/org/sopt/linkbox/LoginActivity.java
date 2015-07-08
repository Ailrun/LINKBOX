package org.sopt.linkbox;

import java.util.Locale;
import com.loopj.android.http.*;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import org.sopt.linkbox.service.LoginFragment;
import org.sopt.linkbox.service.SignupFragment;


public class LoginActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TabHost tabHost = (TabHost)findViewById(R.id.tabHost);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("로그인")
                .setIndicator("log in")
                .setContent(new Intent(this, LoginFragment.class)));

        tabHost.addTab(tabHost.newTabSpec("회원가입")
                .setIndicator("sign up")
                .setContent(new Intent(this, SignupFragment.class)));
    }

   /** SectionsPagerAdapter 내 getItem 을 아래와 같이 수정한다.
    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new RedFragment();
        else if(position==1)
            return new GreenFragment();
        else
            return new BlueFragment();
    }
    */

}
