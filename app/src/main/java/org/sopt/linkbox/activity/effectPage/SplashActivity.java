package org.sopt.linkbox.activity.effectPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loginPage.AccountActivity;
import org.sopt.linkbox.custom.helper.SessionSaver;


public class SplashActivity extends Activity {
    private int SPLASH_TIME=3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Activity savedSession = SessionSaver.recallSession();
        if (savedSession != null) {
            Intent intent = new Intent(this, savedSession.getClass());
            startActivity(intent);
            finish();
            return;
        }

        new Handler().postDelayed(new Runnable() {  // 01 Gives the app how much time the splash screen will be on  (Related to async properties)
            @Override
            public void run() {
                overridePendingTransition(0,android.R.anim.fade_in);
                Intent intent = new Intent(SplashActivity.this, AccountActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
}
