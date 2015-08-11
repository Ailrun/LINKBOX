package org.sopt.linkbox.activity.loginPage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

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

        Typeface typeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");
        EditText etEmailField = (EditText)findViewById(R.id.ET_email_signup);
        EditText etNameField = (EditText)findViewById(R.id.ET_name_signup);
        EditText etPasswordField = (EditText)findViewById(R.id.ET_password_signup);
        EditText etPasswordCheckField = (EditText)findViewById(R.id.ET_password_check_signup);

        etEmailField.setTypeface(typeface);
        etNameField.setTypeface(typeface);
        etPasswordField.setTypeface(typeface);
        etPasswordCheckField.setTypeface(typeface);

        etEmailField.setHintTextColor(getResources().getColor(R.color.hint_black));
        etNameField.setHintTextColor(getResources().getColor(R.color.hint_black));
        etPasswordField.setHintTextColor(getResources().getColor(R.color.hint_black));
        etPasswordCheckField.setHintTextColor(getResources().getColor(R.color.hint_black));

    }
}
