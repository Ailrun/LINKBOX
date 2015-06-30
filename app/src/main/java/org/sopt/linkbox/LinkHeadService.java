package org.sopt.linkbox;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

public class LinkHeadService extends Service {
    private WindowManager windowManager = null;
    private ImageView imageView = null;
    private boolean isImageViewAttached = false;
    private final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    //Property For Touch Listener
    private ImageView exitImageView = null;
    private boolean exitIsImageViewAttached = false;
    private final WindowManager.LayoutParams exitParam = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT);

    @Override
    public void onCreate() {
        super.onCreate();

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        imageView = new ImageView(this);
        imageView.setImageResource(R.mipmap.ic_launcher);

        layoutParams.gravity = Gravity.TOP | Gravity.LEFT;

        addView();

        // TouchListener Implementing Part
        exitImageView = new ImageView(getApplicationContext());
        exitImageView.setImageResource(R.mipmap.ic_launcher);

        exitParam.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;

        imageView.setOnTouchListener(new View.OnTouchListener() {
            private int initialX, initialY;
            private float initialTouchX, initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;
                        initialTouchX = motionEvent.getX();
                        initialTouchY = motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_UP :
                        exitRemoveView();
                        if ((motionEvent.getEventTime() > 1000) &&
                                (Math.abs(layoutParams.x - exitParam.x) < 20) &&
                                (Math.abs(layoutParams.y - exitParam.y) < 20)) {
                            removeView();
                            stopSelf();
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE :
                        if (motionEvent.getEventTime() > 1000) {
                            exitAddView();
                        }
                        layoutParams.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                        layoutParams.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                        windowManager.updateViewLayout(imageView, layoutParams);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addView();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        removeView();
        exitRemoveView();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void addView() {
       if (!isImageViewAttached) {
           windowManager.addView(imageView, layoutParams);
           isImageViewAttached = true;
       }
    }

    private void removeView() {
        if (isImageViewAttached) {
            windowManager.removeView(imageView);
            isImageViewAttached = false;
        }
    }

    private void exitAddView() {
        if (!exitIsImageViewAttached) {
            windowManager.addView(exitImageView, exitParam);
            exitIsImageViewAttached = true;
        }
    }

    private void exitRemoveView() {
        if (exitIsImageViewAttached) {
            windowManager.removeView(exitImageView);
            exitIsImageViewAttached = false;
        }
    }
}
