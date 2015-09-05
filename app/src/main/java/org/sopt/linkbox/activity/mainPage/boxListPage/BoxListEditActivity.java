package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kogitune.activity_transition.ActivityTransitionLauncher;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.mainPage.urlListingPage.LinkBoxActivity;
import org.sopt.linkbox.constant.MainStrings;
import org.sopt.linkbox.custom.adapters.cardViewAdapter.BoxEditBoxListAdapter;
import org.sopt.linkbox.custom.data.mainData.BoxListData;

public class BoxListEditActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + BoxListEditActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private Toolbar tToolbar = null;
    private GridView gvBoxList = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_list_edit);

        initView();
        initControl();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_box_list_edit, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_box:
                startActivity(new Intent(this, BoxAddActivity.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                break;
            case R.id.action_invited_box:
                startActivity(new Intent(this, InvitedBoxActivity.class));
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        // 여기에 코드 입력

        LinkBoxController.inboxIndicator = false;

        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_edit_box);  // TODO : REVIVE THIS PART AFTER FINISHING THE REST
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("내 박스");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        gvBoxList = (GridView) findViewById(R.id.GV_box_box_list_edit);
        gvBoxList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LinkBoxController.currentBox = (BoxListData) gvBoxList.getItemAtPosition(position);
                final Intent intent = new Intent(BoxListEditActivity.this, LinkBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(MainStrings.inBox, true);
                Log.d(TAG, "current Box : " + LinkBoxController.currentBox.toString());
                ActivityTransitionLauncher.with(BoxListEditActivity.this).from(view).launch(intent);
                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            }
        });
    }
    private void initControl() {
        LinkBoxController.boxEditBoxListAdapter =
                new BoxEditBoxListAdapter(getApplicationContext(), LinkBoxController.boxListSource);
        gvBoxList.setAdapter(LinkBoxController.boxEditBoxListAdapter);
    }
    //</editor-fold>
}
