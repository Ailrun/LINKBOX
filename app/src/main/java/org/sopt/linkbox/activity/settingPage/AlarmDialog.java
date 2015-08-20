package org.sopt.linkbox.activity.settingPage;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;

/**
 * Created by MinGu on 2015-08-17.
 */
public class AlarmDialog extends Activity {
    private static final String TAG = "TEST/" + AlarmDialog.class.getName() + " : ";

    private CheckBox cbNewLink = null;
    private CheckBox cbInvitedBox = null;
    private CheckBox cbLike = null;
    private CheckBox cbComment = null;
    private Button bSave = null;
    private Button bCancel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alarm);

        initView();
        initListener();
    }

    private void initView(){
        cbNewLink = (CheckBox) findViewById(R.id.CB_new_link_alarm);
        cbInvitedBox = (CheckBox) findViewById(R.id.CB_invited_box_alarm);
        cbLike = (CheckBox) findViewById(R.id.CB_like_alarm);
        cbComment = (CheckBox) findViewById(R.id.CB_comment_alarm);
        bSave = (Button) findViewById(R.id.B_alarm_save);
        bCancel = (Button) findViewById(R.id.B_alarm_cancel);

        if(LinkBoxController.new_link_alarm){
            cbNewLink.setChecked(true);
        }
        else if(!LinkBoxController.new_link_alarm){
            cbNewLink.setChecked(false);
        }
        if(LinkBoxController.invited_box_alarm){
            cbInvitedBox.setChecked(true);
        }
        else if(!LinkBoxController.invited_box_alarm){
            cbInvitedBox.setChecked(false);
        }
        if(LinkBoxController.like_alarm){
            cbLike.setChecked(true);
        }
        else if(!LinkBoxController.like_alarm){
            cbLike.setChecked(false);
        }
        if(LinkBoxController.comment_alarm){
            cbComment.setChecked(true);
        }
        else if(!LinkBoxController.comment_alarm){
            cbComment.setChecked(false);
        }

    }

    private void initListener(){
        bSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveState();
                finish();
            }
        });

        bCancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void saveState(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        if(cbNewLink.isChecked()){
            editor.putInt("new_link_alarm", 1);
            LinkBoxController.new_link_alarm = true;
        }
        else if(!cbNewLink.isChecked()){
            editor.putInt("new_link_alarm", 0);
            LinkBoxController.new_link_alarm = false;
        }

        if(cbInvitedBox.isChecked()){
            editor.putInt("invited_box_alarm", 1);
            LinkBoxController.invited_box_alarm = true;
        }
        else if(!cbInvitedBox.isChecked()){
            editor.putInt("invited_box_alarm", 0);
            LinkBoxController.invited_box_alarm = false;
        }

        if(cbLike.isChecked()){
            editor.putInt("like_alarm", 1);
            LinkBoxController.like_alarm = true;
        }
        else if(!cbLike.isChecked()){
            editor.putInt("like_alarm", 0);
            LinkBoxController.like_alarm = false;
        }

        if(cbComment.isChecked()){
            editor.putInt("comment_alarm", 1);
            LinkBoxController.comment_alarm = true;
        }
        else if(!cbComment.isChecked()){
            editor.putInt("comment_alarm", 0);
            LinkBoxController.comment_alarm = false;
        }

        editor.commit();
    }
}
