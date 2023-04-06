package com.example.passmeprofessor;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class HorizontalSwipeListener extends GestureDetector.SimpleOnGestureListener {
    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            onSwipeHorizontal(diffX);
            return true;
        }
        return false;
    }

    public void onSwipeHorizontal(float diffX) {
    }
}
