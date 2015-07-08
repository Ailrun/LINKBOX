package org.sopt.linkbox;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


public class UserSettingActivity extends Activity {
    EditText editName = null;
    EditText editMail = null;
    Button changePasswrd = null;
    ImageButton openList = null;
    CheckBox setAllNotification = null;
    ListView notificationLV = null;
    CheckBox hideFloatingBTN = null;
    Button logout = null;
    Button quit = null;

    SharedPreferences sharedPref;
    SharedPreferences.Editor sharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        initView();
        initListener();

    }

    void initView(){
        editName = (EditText)findViewById(R.id.ET_name_user_setting);
        editMail = (EditText)findViewById(R.id.ET_mail_user_setting);
        changePasswrd = (Button)findViewById(R.id.BTN_password_user_setting);
        setAllNotification = (CheckBox)findViewById(R.id.CB_setAll_noti);
        notificationLV = (ListView)findViewById(R.id.LV_notiList_user_setting);
        hideFloatingBTN = (CheckBox)findViewById(R.id.CB_user_setting);

        // editName.setText(); 디비에서 불러와야 하니까 일단 주석
        // editMail.setText();

        sharedPref = getSharedPreferences("userProfile", 0);
        sharedEditor = sharedPref.edit();
    }

    void initListener(){

        /************* 사용자 이름, 메일 수정 리스너 *************/
        editName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editName.setFocusableInTouchMode(true);
                return false;
            }
        });

        editName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    // 이전 설정 이름 삭제
                    sharedEditor.remove("userName");
                    sharedEditor.commit();

                    String name = editName.getText().toString();

                    editName.setText(name);
                    sharedEditor.putString("userName",name);
                    sharedEditor.commit();
                }
                return false;
            }
        });


        editMail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                editName.setFocusableInTouchMode(true);
                return false;
            }
        });

        editMail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if((keyEvent.getAction() == keyEvent.ACTION_DOWN) && (keyCode == keyEvent.KEYCODE_ENTER)) {
                    sharedEditor.remove("userEmail");
                    sharedEditor.commit();

                    String email = editMail.getText().toString();

                    editName.setText(email);
                    sharedEditor.putString("userEmail",email);
                    sharedEditor.commit();
                }
                return false;
            }
        });
        // sharedPref - userProfile파일에 저장하고 막판에 DB 갱신?
        /******************************************************/

        /************** 플로팅 버튼 체크박스 리스너 **************/
        // 아돈노
        /******************************************************/

        // 박스 알림설정 체크박스
        // 박스노티피케이션 리스트뷰 커스텀
        // 로그아웃 버튼
        // 회원탈퇴 버튼
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
