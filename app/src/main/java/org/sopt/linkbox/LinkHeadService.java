package org.sopt.linkbox;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.sopt.linkbox.custom.widget.ClearableEditText;

public class LinkHeadService extends Service {
    private WindowManager wmService = null;
    private InputMethodManager immService = null;

    private ImageView ivService = null;
    private boolean isIvServiceAttached = false;
    private final WindowManager.LayoutParams lpService = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    //Property For Edit
    private ClearableEditText cetEdit = null;
    private boolean isEtEditAttached = false;
    private final WindowManager.LayoutParams lpEdit = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
            WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT
    );
    //Property For Exit
    private ImageView ivExit = null;
    private boolean isivExitAttached = false;
    private final WindowManager.LayoutParams lpExit = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT);
    private DisplayMetrics dmService = new DisplayMetrics();
    private int displayWidth, displayHeight;

    @Override
    public void onCreate() {
        super.onCreate();

        wmService = (WindowManager) getSystemService(WINDOW_SERVICE);
        wmService.getDefaultDisplay().getMetrics(dmService);

        ivService = new ImageView(this);
        ivService.setImageResource(R.mipmap.floating_button);

        int serviceWidth = ((BitmapDrawable)ivService.getDrawable()).getBitmap().getWidth();
        int serviceHeight = ((BitmapDrawable)ivService.getDrawable()).getBitmap().getHeight();
        displayWidth = dmService.widthPixels - serviceWidth;
        displayHeight = dmService.heightPixels - serviceHeight;
//        Toast.makeText(getApplicationContext(), "lvService : " + ivService.getWidth() + ", " + ivService.getHeight(), Toast.LENGTH_SHORT).show();
        lpService.gravity = Gravity.START|Gravity.TOP;
        lpService.x = displayWidth;
        lpService.y = displayHeight;

        addServiceView();

        // TouchListener Implementing Part
        cetEdit = new ClearableEditText(getApplicationContext());
        cetEdit.setHint("http://URL.address/input");
        cetEdit.setBackgroundColor(getResources().getColor(R.color.indigo500));
        cetEdit.setSingleLine();
        cetEdit.setImeOptions(EditorInfo.IME_ACTION_SEND);

        immService = (InputMethodManager) cetEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);
        lpEdit.gravity = Gravity.BOTTOM;
        lpEdit.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

        ivExit = new ImageView(getApplicationContext());
        ivExit.setImageResource(R.mipmap.logo);
        lpExit.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        ivService.setOnTouchListener(new View.OnTouchListener() {
            MotionControlData motionControlData = null;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionControlData == null) {
                    motionControlData = new MotionControlData();
                }
                readyMotionEvent(motionEvent, motionControlData);
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startMotionEvent(motionEvent, motionControlData);
                        return true;
                    case MotionEvent.ACTION_UP:
                        endMotionEvent(motionEvent, motionControlData);
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        whileMotionEvent(motionEvent, motionControlData);
                        return true;
                }
                return false;
            }
        });
        cetEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    removeEditView();
                    Intent intent = new Intent(LinkHeadService.this, LinkItActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
                return false;
            }
        });
        cetEdit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    removeEditView();
                    addServiceView();
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addServiceView();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        removeServiceView();
        removeExitView();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addServiceView() {
        if (!isIvServiceAttached) {
            wmService.addView(ivService, lpService);
            isIvServiceAttached = true;
        }
    }

    private void removeServiceView() {
        if (isIvServiceAttached) {
            wmService.removeView(ivService);
            isIvServiceAttached = false;
        }
    }

    private void addEditView() {
        if (!isEtEditAttached) {
            wmService.addView(cetEdit, lpEdit);
            cetEdit.requestFocus();
            immService.showSoftInputFromInputMethod(cetEdit.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED);
            isEtEditAttached = true;
        }
    }

    private void removeEditView() {
        if (isEtEditAttached) {
            immService.hideSoftInputFromWindow(cetEdit.getWindowToken(), 0);
            wmService.removeView(cetEdit);
            isEtEditAttached = false;
        }
    }

    private void addExitView() {
        if (!isivExitAttached) {
            wmService.addView(ivExit, lpExit);
            isivExitAttached = true;
        }
    }

    private void removeExitView() {
        if (isivExitAttached) {
            wmService.removeView(ivExit);
            isivExitAttached = false;
        }
    }

    private class MotionControlData {
        final long removeTime = 1000;

        int initialX, initialY;
        float initialTouchX, initialTouchY;;
        int removeDiff;
        boolean move;
    }

    private void readyMotionEvent(MotionEvent me, MotionControlData mcd) {
        mcd.removeDiff = ivService.getWidth() > ivService.getHeight() ? ivService.getWidth() : ivService.getHeight();

        if (me.getEventTime() - me.getDownTime() > mcd.removeTime) {
            addExitView();
        }
    }

    private void startMotionEvent(MotionEvent me, MotionControlData mcd) {
        mcd.move =  false;
        mcd.initialX = lpService.x;
        mcd.initialY = lpService.y;
        mcd.initialTouchX = me.getRawX();
        mcd.initialTouchY = me.getRawY();
    }

    private void endMotionEvent(MotionEvent me, MotionControlData mcd) {
//        Toast.makeText(getApplicationContext(), "initial :" + mcd.initialX + ", " + mcd.initialY + "\n"
//        + "lpService : " + lpService.x + ", " + lpService.y, Toast.LENGTH_SHORT).show();
        if ((me.getEventTime() > mcd.removeTime) &&
                (Math.abs(lpService.x - displayWidth / 2) < mcd.removeDiff) &&
                (Math.abs(lpService.y - displayHeight) < mcd.removeDiff)) {
            removeServiceView();
            removeExitView();
            stopSelf();
        }
        if (!mcd.move) {
            addEditView();
            removeServiceView();
            removeExitView();
        }
        removeExitView();
    }

    private void whileMotionEvent(MotionEvent me, MotionControlData mcd) {
        mcd.move = true;
        lpService.x = mcd.initialX + (int) (me.getRawX() - mcd.initialTouchX);
        lpService.x = lpService.x < 0 ? 0 : lpService.x > displayWidth ? displayWidth : lpService.x;
        lpService.y = mcd.initialY + (int) (me.getRawY() - mcd.initialTouchY);
        lpService.y = lpService.y < 0 ? 0 : lpService.y > displayHeight ? displayHeight : lpService.y;
        wmService.updateViewLayout(ivService, lpService);
    }
}
