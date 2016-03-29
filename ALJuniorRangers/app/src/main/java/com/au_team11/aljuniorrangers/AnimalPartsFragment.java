package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsFragment extends Fragment {

    Activity mCallback;

    View view;

    AnimalPartsView animalPartsView;

    Bitmap pic;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Activity) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        view = inflater.inflate(R.layout.animalparts_layout, container, false);

        animalPartsView = (AnimalPartsView) view.findViewById(R.id.animalPartsView);

        animalPartsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*Toast toast = Toast.makeText(mCallback, "X: " + Float.toString(event.getX()) +
                                                        " Y: " + Float.toString(event.getY()) +
                                                        " RX: " + Float.toString(event.getRawX()) +
                                                        " RY: " + Float.toString(event.getRawY()), Toast.LENGTH_SHORT);
                */
                if (animalPartsView.isInPic((int) event.getX(), (int) event.getY())) {
                    Toast toast = Toast.makeText(mCallback, "is in pic", Toast.LENGTH_SHORT);
                    toast.show();
                }
                else {
                    Toast toast = Toast.makeText(mCallback, "is NOT in pic", Toast.LENGTH_SHORT);
                    toast.show();
                }

                return true;
            }
        });

        pic = BitmapFactory.decodeResource(mCallback.getApplicationContext().getResources(), R.drawable.deerimage);

        animalPartsView.setPic(pic);

        return view;
    }
}
