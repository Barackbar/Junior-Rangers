package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity implements ParkListener, ParkActivityListener, CameraRequestListener {

    TrailWalkFragmentArcGIS trailWalkFragment = null;

    FragmentManager fragmentManager;

    String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate");

        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        //TODO: pass filename for object data in constructor arguments
        if (savedInstanceState == null) {
            //put the main menu on screen
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fragmentManager.beginTransaction().add(R.id.activity_main, mainMenuFragment).commit();

            //new wordsearch instantiation
            //TODO: put this into the type.equals("wordsearch") part in onParkActivitySelectedListener
            //WordSearchFragmentJDSS wordSearchFragmentJDSS = new WordSearchFragmentJDSS();
            //Bundle arguments = new Bundle();
            //arguments.putString(getResources().getString(R.string.AssetBundleKey), "wordsearch_test_jdss.json");
            //wordSearchFragmentJDSS.setArguments(arguments);
            //fragmentManager.beginTransaction().add(R.id.activity_main, wordSearchFragmentJDSS).commit();
        }
    }

    @Override
    public void onBackPressed() {
        //will exit the app or pop backstack willy nilly
        super.onBackPressed();

        //will put the park menu on screen

    }

    public void onParkSelectedListener(String parkFileName) {
        //create a new ParkFragment to put on screen
        ParkFragment parkFragment = new ParkFragment();
        //load the arguments bundle with the asset data file name
        Bundle arguments = new Bundle();
        arguments.putString(getResources().getString(R.string.AssetBundleKey), parkFileName);
        parkFragment.setArguments(arguments);
        //put the fragment on the screen
        fragmentManager.beginTransaction()
                       .replace(R.id.activity_main, parkFragment)
                       .addToBackStack(null)
                       .commit();
    }

    public void onParkActivitySelectedListener(String fileName, String type) {

        if (type.equals("trailwalk")) {
            trailWalkFragment = new TrailWalkFragmentArcGIS();
            Bundle arguments = new Bundle();
            arguments.putString(getResources().getString(R.string.AssetBundleKey), fileName);
            trailWalkFragment.setArguments(arguments);
            //put the fragment on the screen
            fragmentManager.beginTransaction()
                           .replace(R.id.activity_main, trailWalkFragment)
                           .addToBackStack(null)
                           .commit();
        }
        else if (type.equals("wordsearch")) {
            WordSearchFragment wordSearchFragment = new WordSearchFragment();

            //new wordsearch instantiation
            //WordSearchFragmentJDSS wordSearchFragmentJDSS = new WordSearchFragmentJDSS();
            //fragmentManager.beginTransaction().add(R.id.activity_main, wordSearchFragmentJDSS).commit();
            //Bundle arguments = new Bundle();
            //arguments.putString(getResources().getString(R.string.AssetBundleKey), fileName);
            //wordSearchFragmentJDSS.setArguments(arguments);

            fragmentManager.beginTransaction()
                           .replace(R.id.activity_main, wordSearchFragment)
                           .addToBackStack(null)
                           .commit();
        }
        else if (type.equals("animalparts")) {
            /*InfoFragment infoFragment = new InfoFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main, infoFragment)
                    .addToBackStack(null)
                    .commit();
            */
            AnimalPartsFragment animalPartsFragment = new AnimalPartsFragment();
            Bundle arguments = new Bundle();
            arguments.putString(getResources().getString(R.string.AssetBundleKey), fileName);
            animalPartsFragment.setArguments(arguments);
            fragmentManager.beginTransaction()
                    .add(R.id.activity_main, animalPartsFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else if (type.equals("progressreport")) {
            ProgressReportFragment progressReportFragment = new ProgressReportFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main, progressReportFragment)
                    .addToBackStack(null)
                    .commit();
        }
        else {
            //do nothing
        }
    }

    public void requestCamera() {
        dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("MainActivity", "oAR");
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Log.i("MainActivity", "rC == 1 && rC == R_OK");
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }
}
