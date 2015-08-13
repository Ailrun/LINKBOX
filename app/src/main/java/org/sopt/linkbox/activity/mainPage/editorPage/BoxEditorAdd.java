package org.sopt.linkbox.activity.mainPage.editorPage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.helper.SessionSaver;


public class BoxEditorAdd extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private EditText etEmail = null;
    private TextView tvMessage = null;

    private SharedPreferences spUserSettings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_editor_add);

        initData();
        initView();
        initListener();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_editor_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_send :
                startActivity(new Intent(this, LinkBoxActivity.class));
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
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

    private void initData() {
        spUserSettings = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.usrListData.usrKey, 0);
    }
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_add);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        etEmail = (EditText) findViewById(R.id.ET_editor_email_editor_add);
        tvMessage = (TextView) findViewById(R.id.ET_sending_message_editor_add);
    }
    private void initListener() {
    }
}
