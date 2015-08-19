package org.sopt.linkbox.activity.helpPage.subHelpPage;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-10.
 */
public class BoxCreateHelpActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + BoxCreateHelpActivity.class.getName() + " : ";

    private Toolbar tToolbar = null;
    private ScrollView svHelpAdd = null;
    private TextView tvAddboxQuestion = null;
    private TextView tvAddboxAnswer_1 = null;
    private TextView tvAddboxAnswer_2 = null;
    private TextView tvEditboxQuestion = null;
    private TextView tvEditboxAnswer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_create_help);

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

        svHelpAdd = (ScrollView) findViewById(R.id.SV_help_add_box);
        tvAddboxQuestion = (TextView)findViewById(R.id.TV_how_to_add_box_question);
        tvAddboxAnswer_1 = (TextView)findViewById(R.id.TV_how_to_add_box_answer_1);
        tvAddboxAnswer_2 = (TextView) findViewById(R.id.TV_how_to_add_box_answer_2);
        tvEditboxQuestion = (TextView) findViewById(R.id.TV_how_to_edit_box_question);
        tvEditboxAnswer = (TextView) findViewById(R.id.TV_how_to_edit_box_answer);

        tvAddboxQuestion.setTextColor(getResources().getColor(R.color.indigo500));
        tvEditboxQuestion.setTextColor(getResources().getColor(R.color.indigo500));

        tvAddboxQuestion.setTypeface(regularTypeface);
        tvAddboxAnswer_1.setTypeface(regularTypeface);
        tvAddboxAnswer_2.setTypeface(regularTypeface);
        tvEditboxQuestion.setTypeface(regularTypeface);
        tvEditboxAnswer.setTypeface(regularTypeface);
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
