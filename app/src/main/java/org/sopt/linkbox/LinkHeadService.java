package org.sopt.linkbox;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.DisplayMetrics;
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
    private DisplayMetrics metrics = new DisplayMetrics();
    private int displayWidth, displayHeight;

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
            private final long removeTime = 1000;
            private int removeDiff;
            private boolean move = false;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                windowManager.getDefaultDisplay().getMetrics(metrics);
                displayWidth = metrics.widthPixels - imageView.getWidth();
                displayHeight = metrics.heightPixels - imageView.getHeight();
                removeDiff = imageView.getWidth() > imageView.getHeight() ? imageView.getWidth() : imageView.getHeight();

                if (motionEvent.getEventTime() - motionEvent.getDownTime() > removeTime) {
                    exitAddView();
                }

                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN :
                        move = false;
                        initialX = layoutParams.x;
                        initialY = layoutParams.y;
                        initialTouchX = motionEvent.getRawX();
                        initialTouchY = motionEvent.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP :
                        if ((motionEvent.getEventTime() > removeTime) &&
                                (Math.abs(layoutParams.x - displayWidth/2) < removeDiff) &&
                                (Math.abs(layoutParams.y - displayHeight) < removeDiff)) {
                            removeView();
                            exitRemoveView();
                            stopSelf();
                        }
                        if (!move) {
                            Intent intent = new Intent(LinkHeadService.this, LinkItActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            removeView();
                            exitRemoveView();
                            startActivity(intent);
                        }
                        exitRemoveView();
                        return true;
                    case MotionEvent.ACTION_MOVE :
                        move = true;
                        layoutParams.x = initialX + (int) (motionEvent.getRawX() - initialTouchX);
                        layoutParams.x = layoutParams.x < 0 ? 0 : layoutParams.x > displayWidth ? displayWidth : layoutParams.x;
                        layoutParams.y = initialY + (int) (motionEvent.getRawY() - initialTouchY);
                        layoutParams.y = layoutParams.y < 0 ? 0 : layoutParams.y > displayHeight ? displayHeight : layoutParams.y;
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
