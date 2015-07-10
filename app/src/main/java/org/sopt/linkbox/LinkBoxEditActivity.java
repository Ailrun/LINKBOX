package org.sopt.linkbox;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.sopt.linkbox.custom.adapters.EditBoxListAdapter;
import org.sopt.linkbox.service.LinkHeadService;


public class LinkBoxEditActivity extends AppCompatActivity {

    Toolbar tToolbar = null;
    ListView lvBoxList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_box_edit);

        stopService(new Intent(getApplicationContext(), LinkHeadService.class));

        initView();
        initControl();
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
    public void onResume() {
        super.onResume();
        stopService(new Intent(getApplicationContext(), LinkHeadService.class));
    }

    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_edit_box);
        setSupportActionBar(tToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        lvBoxList = (ListView) findViewById(R.id.LV_box_list_edit_box);
    }
    private void initControl() {
        LinkBoxController.editBoxListAdapter =
                new EditBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
    }
}
