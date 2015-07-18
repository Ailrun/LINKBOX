package org.sopt.linkbox.custom.listener;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Junyoung on 2015-07-18.
 *
 */
public abstract class OnSwipeTouchListener implements View.OnTouchListener {
    private GestureDetector gestureDetector = null;
    public OnSwipeTouchListener(Context context) {
        gestureDetector = new GestureDetector(context, new SwipeListener());
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class SwipeListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;
        @Override
        public boolean onFling(android.view.MotionEvent e1, android.view.MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
        @Override
        public boolean onDown(android.view.MotionEvent e) {
            return true;
        }
    }
}
