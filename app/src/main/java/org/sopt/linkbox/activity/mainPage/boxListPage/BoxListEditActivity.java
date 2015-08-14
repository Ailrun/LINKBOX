package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
    private Menu menu = null;
    private MenuItem[] menuItems = null;

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
        this.menu = menu;
        menuItems = new MenuItem[menu.size()];
        for (int i = 0; i < menu.size(); i++) {
            menuItems[i] = menu.getItem(i);
        }
        Drawable drawable = getResources().getDrawable(R.drawable.ic_drawer_mybox_24px);
        menuItems[0].setIcon(drawable);
        drawable = getResources().getDrawable(R.drawable.ic_mainpage_editorinfo_24px);
        menuItems[1].setIcon(drawable);
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
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("내 박스");
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
