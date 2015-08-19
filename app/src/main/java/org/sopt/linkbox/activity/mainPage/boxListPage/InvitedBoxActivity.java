package org.sopt.linkbox.activity.mainPage.boxListPage;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.adapters.listViewAdapter.InvitedBoxListAdapter;

/**
 * Created by MinGu on 2015-08-02.
 */
public class InvitedBoxActivity extends AppCompatActivity {
    private static final String TAG = "TEST/" + InvitedBoxActivity.class.getName() + " : ";

    //<editor-fold desc="Private Properties" defaultstate="collapsed">
    private Toolbar tToolbar = null;

    private ListView lvBoxList = null;
    //</editor-fold>

    //<editor-fold desc="Override Methods" defaultstate="collapsed">
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_box_invited_list);

        initView();
        initControl();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
                break;

            default :
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.anim_right_in, R.anim.anim_left_out);
    }
    //</editor-fold>

    //<editor-fold desc="Default Initiate" defaultstate="collapsed">
    private void initView() {
        tToolbar = (Toolbar) findViewById(R.id.T_toolbar_editor_list);  // TODO : REVIVE THIS PART AFTER FINISHING THE REST
        tToolbar.setTitleTextColor(Color.WHITE);
        tToolbar.setTitle("초대받은 박스");
        setSupportActionBar(tToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        lvBoxList = (ListView) findViewById(R.id.LV_invite_box_invite_list);
    }
    private void initControl() {
        LinkBoxController.invitedBoxListAdapter =
                new InvitedBoxListAdapter(getApplicationContext(), LinkBoxController.invitedBoxListSource);
        if (LinkBoxController.invitedBoxListAdapter != null) {
            lvBoxList.setAdapter(LinkBoxController.invitedBoxListAdapter);
        }
    }
    //</editor-fold>

    //<editor-fold desc="Maybe Divided to helper class" defaultstate="collapsed">
    //TODO : DIVIDE?
    private void expand(final View v) {
        //set Visible
        v.setVisibility(View.VISIBLE);


        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(widthSpec, heightSpec);

        ValueAnimator mAnimator = slideAnimator(0, v.getMeasuredHeight(), v);
        mAnimator.start();
    }
    private void collapse(final View v) {
        int finalHeight = v.getHeight();

        ValueAnimator mAnimator = slideAnimator(finalHeight, 0, v);

        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                v.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationStart(Animator animator) {
            }
            @Override
            public void onAnimationCancel(Animator animator) {
            }
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        mAnimator.start();
    }
    private ValueAnimator slideAnimator(int start, int end, final View v) {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
    //</editor-fold>
}
