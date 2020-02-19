package org.afgl.biblioapp.libro.ui;

import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by arturo on 09/06/2017.
 * Detector de gestos
 */
class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;
    private static final int SWIPE_MAX_OF_PATH = 100;

    private MyGestureListener listener;

    MyGestureDetector(MyGestureListener listener){
        this.listener = listener;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffy = e2.getY() - e1.getY();
        float diffx = e2.getX() - e1.getX();
        if(Math.abs(diffx) > Math.abs(diffy)
                && Math.abs(diffx) > SWIPE_THRESHOLD
                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD
                && Math.abs(diffy) < SWIPE_MAX_OF_PATH){
            if(diffx > 0){
                listener.onPrevious();
            } else{
                listener.onNext();
            }
        }
        return super.onFling(e1, e2, velocityX, velocityY);
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        float y = e.getY();
        return listener.pageUpDown(y);
    }

}
