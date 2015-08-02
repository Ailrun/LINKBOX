package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;

public class BoxListEditActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private GridView gvBoxList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_list_edit);

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
    }

    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_edit_box);  // TODO : REVIVE THIS PART AFTER FINISHING THE REST
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gvBoxList = (GridView) findViewById(R.id.GV_box_box_list_edit);
    }

    private void initControl() {
        LinkBoxController.boxEditBoxListAdapter =
                new BoxEditBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);

        gvBoxList.setAdapter(LinkBoxController.boxEditBoxListAdapter);
    }

}
