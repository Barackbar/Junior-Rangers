package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.esri.core.geometry.Point;

import java.io.File;

/**
 * Created by JDSS on 2/26/16.
 */
public class ActionPointPicture extends ActionPoint {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    Fragment fragment;

    public ActionPointPicture(Activity newActivity, Point newLocation, String newText, Fragment newFragment) {
        super(newActivity, newLocation, newText);
        fragment = newFragment;
    }

    public void action() {
        super.action();
        //send camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        //try creating the file
        File photo = null;

        try {
            //photo = File.createTempFile("file", ".jpg", activity.getApplicationContext().getFilesDir());
            //photo = new File(activity.getApplicationContext().getFilesDir(), "file.jpg");
            photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "file.jpg");
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (photo != null) {
            if (takePictureIntent.resolveActivity(activity.getApplicationContext().getPackageManager()) != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                fragment.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
        else {
            Log.i("APP", "photo == null");
        }

    }
}
