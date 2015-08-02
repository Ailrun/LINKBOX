package org.sopt.linkbox.activity.mainPage;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.GridView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditInvitedBoxListAdapter;

/**
 * Created by MinGu on 2015-08-02.
 */
public class InvitedBoxActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;
    private GridView gvBoxList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_invited_list);

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        gvBoxList = (GridView) findViewById(R.id.invited_box_grid_view);
    }

    private void initControl() {
        LinkBoxController.boxEditInvitedBoxListAdapter =
                new BoxEditInvitedBoxListAdapter(getApplicationContext(), LinkBoxController.invitedBoxListSource);

        gvBoxList.setAdapter(LinkBoxController.boxEditInvitedBoxListAdapter);
    }

}
