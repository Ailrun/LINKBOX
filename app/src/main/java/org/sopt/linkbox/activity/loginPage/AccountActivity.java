package org.sopt.linkbox.activity.loginPage;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.helper.SessionSaver;


public class AccountActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private ImageView ivTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_account);
        ivTitle = (ImageView) findViewById(R.id.IV_image_account);
        ivTitle.setImageResource(R.drawable.account_image);
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
