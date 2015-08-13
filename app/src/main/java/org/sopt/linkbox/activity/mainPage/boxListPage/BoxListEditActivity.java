package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;

public class BoxListEditActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private GridView gvBoxList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_list_edit);

        // Moved to onResume
        //initView();
        //initControl();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_list_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.action_add_box :
                startActivity(new Intent(this, BoxAddActivity.class));
                break;
            case R.id.action_invited_box :
                startActivity(new Intent(this, InvitedBoxActivity.class));
                break;
            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
        initView();
        initControl();
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
