package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JDSS on 3/28/16.
 */
public class AnimalPartsFragment extends Fragment {

    Activity mCallback;
    Context context;

    View view;

    AnimalPartsView animalPartsView;

    String filename;

    Bitmap pic;

    //hitboxes, defined in percentage from left and top
    ArrayList<Rect> hitboxes;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (Activity) activity;
            context = activity.getApplicationContext();

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

                Toast toast = Toast.makeText(
                                        mCallback.getApplicationContext(),
                                        "in hitbox " + Integer.toString(animalPartsView.isInBox((int) event.getX(), (int) event.getY(), hitboxes)),
                                        Toast.LENGTH_SHORT);
                toast.show();
                return true;
            }
        });

        filename = getArguments().getString(getResources().getString(R.string.AssetBundleKey));

        String jsonData = loadJSONFromAsset(filename);

        String picName = getPicName(jsonData);

        int picID = context.getResources().getIdentifier(picName, "drawable", context.getPackageName());

        pic = BitmapFactory.decodeResource(mCallback.getApplicationContext().getResources(), picID);

        animalPartsView.setPic(pic);

        hitboxes = buildHitboxes(jsonData);

        /*
        hitboxes = new ArrayList<Rect>();
        hitboxes.add(new Rect(0,0,25,25));
        hitboxes.add(new Rect(25,25,50,50));
        hitboxes.add(new Rect(50,50,75,75));
        hitboxes.add(new Rect(75,75,100,100));
        */

        return view;
    }

    //credit goes to GrlsHu on StackOverflow
    //returns a json string from the asset file
    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String getPicName(String jsonData) {
        try {
            return new JSONObject(jsonData).getString("image");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Rect> buildHitboxes(String jsonData) {
        try {
            ArrayList<Rect> newHitboxes = new ArrayList<Rect>();
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("hitboxes");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                //add a new rect to the list
                newHitboxes.add(
                        new Rect(
                                arrayObject.getInt("left"),
                                arrayObject.getInt("top"),
                                arrayObject.getInt("right"),
                                arrayObject.getInt("bottom")));
            }
            return newHitboxes;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
