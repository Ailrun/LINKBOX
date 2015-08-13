package org.sopt.linkbox.activity.mainPage.editorPage;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.constant.SettingStrings;
import org.sopt.linkbox.custom.adapters.listViewAdapter.LinkEditorListAdapter;
import org.sopt.linkbox.custom.helper.SessionSaver;


public class BoxEditorList extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private ListView lvEditorList = null;
    private TextView tvBoxName = null;
    private String sBox_name = null;
    private String sEditor_number = null;

    private SharedPreferences sharedPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_editor_list);

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
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onStop() {
        super.onStop();
        SessionSaver.saveSession(this);
    }

    private void initData() {
        sharedPreferences = getSharedPreferences(SettingStrings.shared_user_settings
                + LinkBoxController.usrListData.usrKey, 0);
        sBox_name = LinkBoxController.currentBox.boxName;
        sEditor_number = Integer.toString(LinkBoxController.editorListSource.size());

    }
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_list);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        lvEditorList = (ListView) findViewById(R.id.LV_editor_list_editor_list);

        tvBoxName = (TextView) findViewById(R.id.TV_box_name_editor_list);
        tvBoxName.setText(sBox_name + " (" + sEditor_number + ")");

    }
    private void initListener() {
    }
    private void initControl() {
        LinkBoxController.linkEditorListAdapter =
                new LinkEditorListAdapter(getApplicationContext(), LinkBoxController.editorListSource);
    }
}
