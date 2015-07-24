package org.sopt.linkbox.activity.effectPage;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.TransitionManager;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.loginPage.MainActivity;
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
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                overridePendingTransition(0,android.R.anim.fade_in);
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME);
    }
}