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

/**
 * Created by JDSS on 2/26/16.
 */
public class ActionPointPicture extends ActionPoint {

    Boolean pictureTaken;
    String photoFileName;
    String extension = ".jpg";
    String pathToDirectory;
    String pathToFile;
    File imageFile;

    public ActionPointPicture(Activity newActivity, Point newLocation, String newText, String newFileName) {
        super(newActivity, newLocation, newText);
        pathToDirectory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        photoFileName = newFileName;
        pathToFile = pathToDirectory + "/" + photoFileName + extension;
        //if this picture exists
        if (new File(pathToFile).exists())
            //indicate it exists
            pictureTaken = true;
        else
            pictureTaken = false;
    }

    public void action() {
        super.action();

        try {
            imageFile = createImageFile(photoFileName);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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
        File storageDir = new File(pathToDirectory);
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
        this.pathToDirectory = image.getAbsolutePath();
        Log.i("APP", this.pathToDirectory);

        return image;
    }

}
