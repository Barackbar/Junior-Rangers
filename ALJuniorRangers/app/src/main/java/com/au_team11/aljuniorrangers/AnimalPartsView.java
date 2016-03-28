package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsView extends View {

    //the picture to show on screen
    Bitmap pic;

    public AnimalPartsView(Context context, Bitmap newPic) {
        super(context);

        pic = newPic;
    }

    /* Create the rect into which the bitmap will be drawn
     *
     */
    private void scalePic(Canvas canvas) {



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        //these should give x,y relative to view bounds
        event.getX();
        event.getY();
        //these should give x,y relative to screen
        event.getRawX();
        event.getRawY();
        //these should give view bounds
        this.getLeft();
        this.getTop();

        return true;
    }
}
