package org.sopt.linkbox.activity.helpPage.subHelpPage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-10.
 */
public class StartHelpActivity extends AppCompatActivity{
    private Toolbar tToolbar = null;

    private TextView tvHowToAddQuestion = null;
    private TextView tvHowToAddAnswer = null;
    private TextView tvWhereShareQuestion = null;
    private TextView tvWhereShareAnswer = null;
    private ImageView ivHowToAdd = null;
    private ImageView ivWhereShare = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_help);

        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_link_box);
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initHelpContents();
    }

    public void initHelpContents(){

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

}
