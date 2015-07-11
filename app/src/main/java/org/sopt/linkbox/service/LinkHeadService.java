package org.sopt.linkbox.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.sopt.linkbox.LinkItActivity;
import org.sopt.linkbox.R;
import org.sopt.linkbox.custom.widget.ClearableEditText;

/*
 * can be Single?
 */
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
    //Property For Delete
    private ImageView ivDelete = null;
    private boolean isivDeleteAttached = false;
    private final WindowManager.LayoutParams lpDelete = new WindowManager.LayoutParams(
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
        initWindow();

        initView();
        initListener();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addServiceView();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        removeServiceView();
        removeDeleteView();
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void initWindow() {
        wmService = (WindowManager) getSystemService(WINDOW_SERVICE);
        wmService.getDefaultDisplay().getMetrics(dmService);
    }
    private void initView() {
        // Image Setting Part
        initServiceView();
        addServiceView();

        // TouchListener Setting Part
        initEditView();

        // Delete Image Setting Part
        initDeleteView();
    }
    private void initListener() {
        ivService.setOnTouchListener(new View.OnTouchListener() {
            class MotionControlData {
                final long removeTime = 1000;

                int initialX, initialY;
                float initialTouchX, initialTouchY;;
                int removeDiff;
                boolean move;
            }

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

            private void readyMotionEvent(MotionEvent me, MotionControlData mcd) {
                mcd.removeDiff = ivService.getWidth() > ivService.getHeight() ? ivService.getWidth() : ivService.getHeight();

                if (me.getEventTime() - me.getDownTime() > mcd.removeTime) {
                    addDeleteView();
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
                if ((me.getEventTime() > mcd.removeTime) &&
                        (Math.abs(lpService.x - displayWidth / 2) < mcd.removeDiff) &&
                        (Math.abs(lpService.y - displayHeight) < mcd.removeDiff)) {
                    removeServiceView();
                    removeDeleteView();
                    stopSelf();
                }
                if (!mcd.move) {
                    addEditView();
                    removeServiceView();
                    removeDeleteView();
                }
                removeDeleteView();
            }

            private void whileMotionEvent(MotionEvent me, MotionControlData mcd) {
                mcd.move = ((lpService.x - mcd.initialX)*(lpService.x - mcd.initialX)
                + (lpService.y - mcd.initialY)*(lpService.y - mcd.initialY) > 50);
                lpService.x = mcd.initialX + (int) (me.getRawX() - mcd.initialTouchX);
                lpService.x = lpService.x < 0 ? 0 : lpService.x > displayWidth ? displayWidth : lpService.x;
                lpService.y = mcd.initialY + (int) (me.getRawY() - mcd.initialTouchY);
                lpService.y = lpService.y < 0 ? 0 : lpService.y > displayHeight ? displayHeight : lpService.y;
                wmService.updateViewLayout(ivService, lpService);
            }

        });
        cetEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    String str = cetEdit.getText().toString();
                    removeEditView();
                    Intent intent = new Intent(LinkHeadService.this, LinkItActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(Intent.EXTRA_TEXT, str);
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

    private void initServiceView() {
        ivService = new ImageView(this);
        ivService.setImageResource(R.mipmap.floating_button);

        int serviceWidth = ((BitmapDrawable)ivService.getDrawable()).getBitmap().getWidth();
        int serviceHeight = ((BitmapDrawable)ivService.getDrawable()).getBitmap().getHeight();
        displayWidth = dmService.widthPixels - serviceWidth;
        displayHeight = dmService.heightPixels - serviceHeight;
        lpService.gravity = Gravity.START|Gravity.TOP;
        lpService.x = displayWidth;
        lpService.y = displayHeight;
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

    private void initEditView() {
        cetEdit = new ClearableEditText(getApplicationContext());
        cetEdit.setHint("http://URL.address/input");
        cetEdit.setBackgroundColor(getResources().getColor(R.color.indigo500));
        cetEdit.setSingleLine();
        cetEdit.setImeOptions(EditorInfo.IME_ACTION_SEND);

        immService = (InputMethodManager) cetEdit.getContext().getSystemService(INPUT_METHOD_SERVICE);
        lpEdit.gravity = Gravity.BOTTOM;
        lpEdit.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;
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

    private void initDeleteView() {
        ivDelete = new ImageView(getApplicationContext());
        ivDelete.setImageResource(R.mipmap.logo);
        lpDelete.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    }
    private void addDeleteView() {
        if (!isivDeleteAttached) {
            wmService.addView(ivDelete, lpDelete);
            isivDeleteAttached = true;
        }
    }
    private void removeDeleteView() {
        if (isivDeleteAttached) {
            wmService.removeView(ivDelete);
            isivDeleteAttached = false;
        }
    }
}
