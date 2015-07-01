package org.sopt.linkbox;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by user on 2015-06-30.
 */
public class LinkBoxActivity extends Activity {


    DrawerLayout drawerLayout;

    TextView boxNameView;
    EditText editUrl;
    ImageButton addUrl,invite,editorsInfo;
    ListView urlList;

    View drawerView;
    EditText boxName;
    Button addBox;
    ImageButton toSettings;
    ListView drawerItem;

    BoxListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_link_box);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout_Main);
        boxNameView = (TextView)findViewById(R.id.boxNameView);
        editUrl = (EditText)findViewById(R.id.editUrl);
        addUrl = (ImageButton)findViewById(R.id.addUrl);
        invite = (ImageButton)findViewById(R.id.invite);
        editorsInfo = (ImageButton)findViewById(R.id.editorsInfo);
        urlList = (ListView)findViewById(R.id.urlList);

        drawerView = (View)findViewById(R.id.drawer);
        boxName = (EditText)findViewById(R.id.boxName);
        addBox = (Button)findViewById(R.id.addBox);
        toSettings = (ImageButton)findViewById(R.id.toSettings);
        drawerItem = (ListView)findViewById(R.id.drawerItem);




    }





}
