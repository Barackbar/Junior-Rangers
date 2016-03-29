package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsView extends View {

    //the picture to show on screen
    Bitmap pic;

    Boolean picScaled = false;

    //rectangle that holds the bitmap pic, note this is relative to the canvas provided in onDraw
    Rect destRect;
    Rect srcRect;

    public AnimalPartsView(Context context) {
        super(context);
    }

    public AnimalPartsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimalPartsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public Boolean isInPic(int x, int y) {

        //TODO: make this method take in an arraylist of rects maybe, and return back the index of
        //the rect it is in or -1 for inside none

        if (x > destRect.left && x < destRect.right/2 && y > destRect.top && y < destRect.bottom/2) {
            return true;
        }

        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (!picScaled)
            scalePic(canvas);

        canvas.drawBitmap(pic, null, destRect, null);

    }

    private void scalePic(Canvas canvas) {

        picScaled = true;

        //create rect for picture
        destRect = new Rect();

        //srcRect = new Rect(0, 0, pic.getWidth(), pic.getHeight());


        //find width/height of picture
        //find width/height of canvas
        //if canvas w/h > picture w/h
            //destRect L & R = 0 & canvas.width
            //destRect height = destRect width / pic w/h
            //destRect top = (canvas.height - destRect height) / 2
            //destRect bottom  = canvas.height - ((canvas.height - destRect height) / 2)

        double picWHR = (double) pic.getWidth() / pic.getWidth();
        double canvasWHR = (double) canvas.getWidth() / canvas.getHeight();

        if (canvasWHR <= picWHR) {
            destRect.left = 0;
            destRect.right = canvas.getWidth();
            int dRHeight = destRect.width() / (int) picWHR;
            destRect.top = (canvas.getHeight() - dRHeight) / 2;
            destRect.bottom = canvas.getHeight() - ((canvas.getHeight() - dRHeight) / 2);
        }
        else {

        }

    }
}
