package org.sopt.linkbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.widget.EditText;
import android.widget.TextView;

import org.sopt.linkbox.service.LinkHeadService;


public class LinkEditorAdd extends AppCompatActivity {

    Toolbar tToolbar = null;
    EditText etEmail = null;
    TextView tvMessage = null;

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_editor_add);
        initData();
        initView();
        initListener();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link_editor_add, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (sharedPreferences.getBoolean("floating", true)) {
            startService(new Intent(getApplicationContext(), LinkHeadService.class));
        }
    }

    private void initData() {
        sharedPreferences = getSharedPreferences(getResources().getString(R.string.sharedProfile)
                + LinkBoxController.linkUserData.usrid, 0);
    }
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_add);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        etEmail = (EditText) findViewById(R.id.ET_editor_email_editor_add);
        tvMessage = (TextView) findViewById(R.id.ET_sending_message_editor_add);
    }
    private void initListener() {
    }
}
