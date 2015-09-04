package org.sopt.linkbox.activity.helpPage.subHelpPage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-10.
 */
public class LinkOpenHelpActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + LinkOpenHelpActivity.class.getName() + " : ";

    private Toolbar tToolbar = null;
    private TextView tvQuestion = null;
    private TextView tvAnswer1 = null;
    private TextView tvAnswer2 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_open_help);


        initView();
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView() {
        initToolbarView();
        initHelpContentsView();
    }
    //</editor-fold>
    //<editor-fold desc="Initiate Toolbar" defaultstate="collapsed">
    private void initToolbarView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("도움말");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initHelpContentsView(){
        // Typeface boldTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Bold-Hestia.otf");
        Typeface regularTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        tvQuestion = (TextView)findViewById(R.id.TV_how_to_open_link_question);
        tvAnswer1 = (TextView)findViewById(R.id.TV_how_to_open_link_answer_1);
        tvAnswer2 = (TextView)findViewById(R.id.TV_how_to_open_link_answer_2);

        tvQuestion.setTextColor(getResources().getColor(R.color.indigo500));

        tvQuestion.setTypeface(regularTypeface);
        tvAnswer1.setTypeface(regularTypeface);
        tvAnswer2.setTypeface(regularTypeface);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_top_in, R.anim.anim_bottom_out);
                break;

            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_top_in, R.anim.anim_bottom_out);
    }
}
