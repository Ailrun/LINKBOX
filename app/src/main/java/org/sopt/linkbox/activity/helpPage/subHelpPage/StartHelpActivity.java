package org.sopt.linkbox.activity.helpPage.subHelpPage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-10.
 *
 */
public class StartHelpActivity extends AppCompatActivity{
    private static final String TAG = "TEST/" + StartHelpActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private Toolbar tToolbar = null;
    private TextView tvHowToAddQuestion = null;
    private TextView tvHowToAddAnswer = null;
    private TextView tvWhereShareQuestion = null;
    private TextView tvWhereShareAnswer = null;
    private ImageView ivHowToAdd = null;
    private ImageView ivWhereShare = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_help);

        initView();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();

                break;

            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();

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
    //</editor-fold>
    //<editor-fold desc="Initiate Help Contents" defaultstate="collapsed">
    private void initHelpContentsView(){
        // Typeface boldTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Bold-Hestia.otf");
        Typeface regularTypeface = Typeface.createFromAsset(getAssets(), "NotoSansKR-Regular-Hestia.otf");

        tvHowToAddQuestion = (TextView) findViewById(R.id.TV_how_to_add_link_question);
        tvHowToAddAnswer = (TextView) findViewById(R.id.TV_how_to_add_link_answer);
        tvWhereShareQuestion = (TextView) findViewById(R.id.TV_where_share_button_question);
        tvWhereShareAnswer = (TextView) findViewById(R.id.TV_where_share_button_answer);
        ivHowToAdd = (ImageView) findViewById(R.id.IV_how_to_add_link);
        ivWhereShare = (ImageView) findViewById(R.id.IV_where_share_button);

        tvHowToAddQuestion.setTextColor(getResources().getColor(R.color.indigo500));
        tvWhereShareQuestion.setTextColor(getResources().getColor(R.color.indigo500));

        tvHowToAddQuestion.setTypeface(regularTypeface);
        tvWhereShareQuestion.setTypeface(regularTypeface);
        tvHowToAddAnswer.setTypeface(regularTypeface);
        tvWhereShareAnswer.setTypeface(regularTypeface);
    }
    //</editor-fold>
}
