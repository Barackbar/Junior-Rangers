package com.au_team11.aljuniorrangers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsView extends View {

    //the amount of canvas size difference to cause a re-scaling of the picture
    public static final double EPSILON = 0.00001;

    //the picture to show on screen
    Bitmap pic;

    Boolean picScaled = false;

    //rectangle that holds the bitmap pic, note this is relative to the canvas provided in onDraw
    Rect destRect;
    //the width to height ratio of the canvas
    double canvasWHR;
    //the width to height ratio of the picture
    double picWHR;


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

    public int isInBox(int x, int y, ArrayList<Rect> hitboxes) {

        //search through the hitboxes
        for (int i = 0; i < hitboxes.size(); i++) {
            //if x,y is within the current hitbox area (hitboxes defined in percentage)
            //x and y are integer pixels, divide by width or height to get decimal percentage from left or top
            //multiply by 100 to get integer percentage, which the hitboxes are defined as
            if (hitboxes.get(i).contains((int) (((double) x / destRect.width()) * 100),
                                         (int) (((double) y / destRect.height()) * 100))) {
                return i;
            }
        }

        //if it can't find a hitbox that contains x,y, return -1
        return -1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //if the difference between the previous width/height is greater than epsilon, re-scale destRect
        if (Math.abs(canvasWHR - ((double) canvas.getWidth() / (double) canvas.getHeight())) > EPSILON)
            scalePic(canvas);

        canvas.drawBitmap(pic, null, destRect, null);
    }

    private void scalePic(Canvas canvas) {

        picScaled = true;

        //create rect to hold picture
        destRect = new Rect();

        //find the width to height ratios of the picture and provided canvas
        picWHR = (double) pic.getWidth() / pic.getWidth();
        canvasWHR = (double) canvas.getWidth() / canvas.getHeight();

        //if the canvas is skinnier than or equal to the picture
        if (canvasWHR <= picWHR) {
            destRect.left = 0;
            destRect.right = canvas.getWidth();
            int dRHeight = (int) (destRect.width() / picWHR);
            destRect.top = (canvas.getHeight() - dRHeight) / 2;
            destRect.bottom = canvas.getHeight() - ((canvas.getHeight() - dRHeight) / 2);
        }
        //else the canvas is wider than the picture
        else {
            destRect.top = 0;
            destRect.bottom = canvas.getHeight();
            int dRWidth = (int) (destRect.height() * picWHR);
            destRect.left = (canvas.getWidth() - dRWidth) / 2;
            destRect.right = canvas.getWidth() - ((canvas.getWidth() - dRWidth) / 2);
        }
    }
}
