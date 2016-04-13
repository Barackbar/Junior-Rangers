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

    public ActionPointPicture(Activity newActivity, Point newLocation, String newText, String newFileName) {
        super(newActivity, newLocation, newText);
        pictureTaken = false;
        //create the image file to save into
        photoFileName = newFileName;
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
        //make sure something can take care of this intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            //if the image file was successfully created
            if (imageFile != null) {
                //put the file in the intent
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                //send off the intent
                activity.startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile(String fileName) throws IOException {

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
