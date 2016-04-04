package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity implements ParkListener, ParkActivityListener {

    TrailWalkFragmentArcGIS trailWalkFragment = null;

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MainActivity", "onCreate");

        setContentView(R.layout.activity_main);

        fragmentManager = getFragmentManager();

        //create a new TrailWalkFragment
        //TODO: pass filename for object data in constructor arguments
        if (savedInstanceState == null) {
            //put the main menu on screen
            MainMenuFragment mainMenuFragment = new MainMenuFragment();
            fragmentManager.beginTransaction().add(R.id.activity_main, mainMenuFragment).commit();
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
            fragmentManager.beginTransaction()
                           .replace(R.id.activity_main, wordSearchFragment)
                           .addToBackStack(null)
                           .commit();
        }
        else if (type.equals("animalparts")) {
            InfoFragment infoFragment = new InfoFragment();
            fragmentManager.beginTransaction()
                    .replace(R.id.activity_main, infoFragment)
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
}
