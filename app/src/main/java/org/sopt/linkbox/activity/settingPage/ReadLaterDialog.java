package org.sopt.linkbox.activity.settingPage;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;

import org.sopt.linkbox.LinkBoxController;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.data.mainData.UsrListData;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by MinGu on 2015-08-17.
 */
public class ReadLaterDialog extends Activity {
    private static final String TAG = "TEST/" + ReadLaterDialog.class.getName() + " : ";

    private RadioButton rbHour = null;
    private RadioButton rbTwoHour = null;
    private RadioButton rbTomorrow = null;
    private RadioButton rbTwoTomorrow = null;
    private Button bSave = null;
    private Button bCancel = null;

    private int readLaterIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_read_later);

        initView();
        initListener();
        saveState();
    }

    public void initView(){
        rbHour = (RadioButton) findViewById(R.id.RB_hour_later);
        rbTwoHour = (RadioButton) findViewById(R.id.RB_2hours_later);
        rbTomorrow = (RadioButton) findViewById(R.id.RB_tomorrow);
        rbTwoTomorrow = (RadioButton) findViewById(R.id.RB_2tomorrow);
        bSave = (Button) findViewById(R.id.B_read_later_save);
        bCancel = (Button) findViewById(R.id.B_read_later_cancel);

        if(LinkBoxController.preference_readLater == 1){
            rbHour.setChecked(true);
        }
        else if(LinkBoxController.preference_readLater == 2){
            rbTwoHour.setChecked(true);
        }
        else if(LinkBoxController.preference_readLater == 3){
            rbTomorrow.setChecked(true);
        }
        else if(LinkBoxController.preference_readLater == 4){
            rbTwoTomorrow.setChecked(true);
        }
    }

    public void initListener(){

        rbHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbTwoHour.setChecked(false);
                rbTomorrow.setChecked(false);
                rbTwoTomorrow.setChecked(false);
            }
        });

        rbTwoHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbHour.setChecked(false);
                rbTomorrow.setChecked(false);
                rbTwoTomorrow.setChecked(false);
            }
        });

        rbTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbHour.setChecked(false);
                rbTwoHour.setChecked(false);
                rbTwoTomorrow.setChecked(false);
            }
        });

        rbTwoTomorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rbHour.setChecked(false);
                rbTwoHour.setChecked(false);
                rbTomorrow.setChecked(false);
            }
        });

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

    public void saveState(){
        if(rbHour.isChecked() == true){
            readLaterIndicator = 1;
        }
        else if(rbTwoHour.isChecked() == true){
            readLaterIndicator = 2;
        }
        else if(rbTomorrow.isChecked() == true){
            readLaterIndicator = 3;
        }
        else if(rbTwoTomorrow.isChecked() == true){
            readLaterIndicator = 4;
        }

        // Change state of the controller
        LinkBoxController.preference_readLater = readLaterIndicator;

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("read_later_preference", readLaterIndicator);
        editor.commit();
    }
}
