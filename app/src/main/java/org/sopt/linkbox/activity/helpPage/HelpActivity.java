package org.sopt.linkbox.activity.helpPage;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sopt.linkbox.R;
import org.sopt.linkbox.activity.helpPage.subHelpPage.BoxCreateHelpActivity;
import org.sopt.linkbox.activity.helpPage.subHelpPage.BoxShareHelpActivity;
import org.sopt.linkbox.activity.helpPage.subHelpPage.LinkOpenHelpActivity;
import org.sopt.linkbox.activity.helpPage.subHelpPage.StartHelpActivity;
/**
 * Created by MinGu on 2015-08-10.
 *
 */
public class HelpActivity extends AppCompatActivity {
    private Toolbar tToolbar = null;

    // Help Contents
    private TextView tvStartHelp = null;
    private TextView tvFunctionsHelp = null;
    private TextView tvSupportHelp = null;
    private RelativeLayout rlStartHelp = null;
    private RelativeLayout rlLinkboxLinkOpenHelp = null;
    private RelativeLayout rlAddBoxHelp = null;
    private RelativeLayout rlShareBoxHelp = null;
    private RelativeLayout rlSupportHelp = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_main);

        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initHelpContents();
        initHelpListener();
    }

    public void initHelpContents(){

        // Typeface boldTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Bold-Hestia.otf");
        Typeface regularTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        tvStartHelp = (TextView) findViewById(R.id.TV_start_help);
        tvFunctionsHelp = (TextView) findViewById(R.id.TV_functions_help);
        tvSupportHelp = (TextView) findViewById(R.id.TV_support_help);
        rlStartHelp = (RelativeLayout) findViewById(R.id.RL_start_help);
        rlLinkboxLinkOpenHelp = (RelativeLayout) findViewById(R.id.RL_open_linkbox_help);
        rlAddBoxHelp = (RelativeLayout) findViewById(R.id.RL_add_to_box_help);
        rlShareBoxHelp = (RelativeLayout) findViewById(R.id.RL_share_help);
        rlSupportHelp = (RelativeLayout) findViewById(R.id.RL_support_help);

        tvStartHelp.setText(R.string.start_help);
        tvFunctionsHelp.setText(R.string.functions_help);
        tvSupportHelp.setText(R.string.support_help);

        tvStartHelp.setTextColor(getResources().getColor(R.color.teal400));
        tvFunctionsHelp.setTextColor(getResources().getColor(R.color.teal400));
        tvSupportHelp.setTextColor(getResources().getColor(R.color.teal400));

        tvStartHelp.setTypeface(regularTypeface);
        tvFunctionsHelp.setTypeface(regularTypeface);
        tvSupportHelp.setTypeface(regularTypeface);

    }

    public void initHelpListener(){
        rlStartHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StartHelpActivity.class));
            }
        });
        rlLinkboxLinkOpenHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LinkOpenHelpActivity.class));
            }
        });
        rlAddBoxHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BoxCreateHelpActivity.class));
            }
        });
        rlShareBoxHelp.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), BoxShareHelpActivity.class));
            }
        });
        rlSupportHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("vnd.android.cursor.item/email");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"fullstackfactory@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Linkbox 버그 리포트입니다");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "Send mail using..."));
            }
        });

    }
}
