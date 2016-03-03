package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.esri.core.geometry.Point;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JDSS on 2/26/16.
 */
public class ActionPointPicture extends ActionPoint {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public String picturePath;

    public ActionPointPicture(Activity newActivity, Point newLocation, String newText) {
        super(newActivity, newLocation, newText);
    }

    public void action() {
        super.action();
        //send camera intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getApplicationContext().getPackageManager()) != null) {
            //give the intent a file to store the photo into
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //modified from the Android "Taking Photos Simply" guide
    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "image";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        /*
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        */
        File image = new File(storageDir, "image.jpg");

        // Save a file: path for use with ACTION_VIEW intents
        picturePath = image.getAbsolutePath();
        return image;
    }
}
