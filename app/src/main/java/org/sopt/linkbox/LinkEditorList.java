package org.sopt.linkbox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.sopt.linkbox.custom.adapters.LinkEditorListAdapter;
import org.sopt.linkbox.service.LinkHeadService;

import java.util.ArrayList;


public class LinkEditorList extends AppCompatActivity {

    Toolbar tToolbar = null;
    ListView lvEditorList = null;

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_editor_list);

        initData();
        initView();
        initListener();
        initControl();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_link_editor_list, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
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
        LinkBoxController.editorListSource.add(LinkBoxController.currentBox, new ArrayList<String>());
    }
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_list);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvEditorList = (ListView) findViewById(R.id.LV_editor_list_editor_list);
    }
    private void initListener() {
    }
    private void initControl() {
        LinkBoxController.linkEditorListAdapter =
                new LinkEditorListAdapter(getApplicationContext(), LinkBoxController.editorListSource.get(LinkBoxController.currentBox));
    }
}
