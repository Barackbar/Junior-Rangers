package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Camera;
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

    Boolean pictureTaken;
    String photoFileName;
    String extension = ".jpg";
    String mCurrentPhotoPath;
    File imageFile;

    public ActionPointPicture(Activity newActivity, Point newLocation, String newText) {
        super(newActivity, newLocation, newText);
        pictureTaken = false;
        //create the image file to save into
        photoFileName = "muhPic";
        try {
            imageFile = createImageFile(photoFileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void action() {
        super.action();
        //if (activity instanceof CameraRequestListener)
            //((CameraRequestListener) activity).requestPhotoSuccess();
        dispatchTakePictureIntent();

    }

    public void checkIfPictureTaken() {
        //if the photo was taken successfully
        if (activity instanceof CameraRequestListener) {
            Log.i("APP", "activity instanceof CRL");
            //if the picture has been successfully taken once, it should stay that way
            pictureTaken = pictureTaken || ((CameraRequestListener) activity).requestPhotoSuccess();
        }
        else {
            Log.i("APP", "activity NOT instanceof CRL");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {

            /*
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            */

            if (imageFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                activity.startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile(String fileName) throws IOException {

        //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        //String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        //create the file
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image;


        if (new File(storageDir.getAbsolutePath() + "/" + fileName + extension).exists()) {
            Log.i("APP", "file already exists");
        }
        else {
            Log.i("APP", "file DOES NOT already exist");
        }


        //File image = File.createTempFile(fileName, ".jpg", storageDir);
        image = new File(storageDir.getAbsolutePath() + "/" + fileName + extension);


        //set the photo path
        this.mCurrentPhotoPath = image.getAbsolutePath();
        Log.i("APP", this.mCurrentPhotoPath);

        return image;
    }

}
