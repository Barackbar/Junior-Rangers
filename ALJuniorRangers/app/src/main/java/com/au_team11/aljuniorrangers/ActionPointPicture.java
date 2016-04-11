package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

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
    static final int REQUEST_TAKE_PHOTO = 1;

    String mCurrentPhotoPath;

    public ActionPointPicture(CameraRequestListener newActivity, Point newLocation, String newText) {
        super(newActivity, newLocation, newText);
    }

    public void action() {
        super.action();

        activity.requestCamera();

        //dispatchTakePictureIntent();

        //send camera intent
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if (takePictureIntent.resolveActivity(activity.getApplicationContext().getPackageManager()) != null) {
        //    activity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
    }


    /*
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getApplicationContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                activity.startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
    */
}
