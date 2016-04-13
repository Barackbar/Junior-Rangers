package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.PopupContainer;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by JDSS on 2/17/16.
 */
public class TrailWalkFragmentArcGIS extends Fragment {

    private static Context context;

    //how close a click must be to a point to trigger an action
    public static final float NEARBY_RADIUS_DP = 10;

    //Parent Activity
    Activity activity;

    //used in isNearOnScreen for calculating proximity
    int pxPerDp;

    View view;

    //popup for normal ActionPoint
    PopupMenu popup;

    //textview where "popup" will dump its info
    TextView actionPointPopup;
    //the above textview container
    ScrollView actionPointPopupContainer;
    //button used to close the popup
    Button closeWindowButton;
    //button used for actionpointpictures to view the picture, if taken already
    Button viewPictureButton;
    //place to put the picture that was taken
    ImageView imageView;
    //whether the picture is on scrren or not
    Boolean picOnScreen = false;

    //used to test REST data requests
    //String featureServiceURL0 = "https://conservationgis.alabama.gov/adcnrweb/rest/services/Trails_SLD/MapServer/2";
    String featureServiceURL1 = "https://conservationgis.alabama.gov/adcnrweb/rest/services/Trails_SLD/MapServer/1";
    //ArcGISFeatureLayer featureLayer0;
    ArcGISFeatureLayer featureLayer1;

    //the map on screen
    MapView mapView;
    PopupContainer popupContainer;
    //controls displaying the current device location
    LocationDisplayManager locationDisplayManager;
    //how the map is drawn
    SpatialReference spatialReference;

    //trail data filename
    String fileName;
    //Points defined by the JSON file
    ArrayList<ActionPoint> actionPoints;
    GraphicsLayer graphicsLayer;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity =  activity;
        pxPerDp =   activity.getApplicationContext().getResources().getDisplayMetrics().densityDpi
                  / activity.getApplicationContext().getResources().getDisplayMetrics().DENSITY_DEFAULT;
        context = activity.getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //check if view already exists
        if (view != null) {
            ((ViewGroup) view.getParent()).removeView(view);
            return view;
        }

        //inflate view from layout
        view = inflater.inflate(R.layout.trailwalk_layout_arcgis, container, false);


        imageView = (ImageView) view.findViewById(R.id.imageView);


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("TRAIL", 25);
        editor.commit();

        //get textview from layout
        actionPointPopup = (TextView) view.findViewById(R.id.ActionButtonPopup);
        //get the textview container from layout
        actionPointPopupContainer = (ScrollView) view.findViewById(R.id.ActionButtonPopupContainer);
        //set it to GONE to prevent clicks
        actionPointPopupContainer.setVisibility(View.INVISIBLE);

        closeWindowButton = (Button) view.findViewById(R.id.CloseWindowButton);
        //when clicked, should "close" the info window
        closeWindowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set everything invisible
                imageView.setVisibility(View.INVISIBLE);
                closeWindowButton.setVisibility(View.INVISIBLE);
                actionPointPopupContainer.setVisibility(View.INVISIBLE);
                imageView.setVisibility(View.INVISIBLE);
                //since picture is off screen, needs to reset to show it can be put on screen
                viewPictureButton.setText("View Picture");
                picOnScreen = false;
            }
        });
        closeWindowButton.setVisibility(View.INVISIBLE);

        viewPictureButton = (Button) view.findViewById(R.id.ViewPictureButton);

        //get json file to use
        fileName = getArguments().getString(getResources().getString(R.string.AssetBundleKey));
        //load json into a string
        final String jsonData = loadJSONFromAsset(fileName);

        //get reference to the map
        mapView = (MapView) view.findViewById(R.id.map);
        //mapview initialization must occur only after the map is ready
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == mapView && status == STATUS.INITIALIZED) {
                    //get the spatial reference of the map to sync with layers
                    spatialReference = mapView.getSpatialReference();
                    //get location display manager
                    locationDisplayManager = mapView.getLocationDisplayManager();
                    //tell the map to show the current location
                    locationDisplayManager.setShowLocation(true);
                    //start location tracking
                    locationDisplayManager.start();
                    //center the map at the point defined by the json file
                    centerMap(jsonData, mapView);
                }
            }
        });

        //add REST requested feature layer
        //featureLayer0 = new ArcGISFeatureLayer(featureServiceURL0, ArcGISFeatureLayer.MODE.ONDEMAND);
        featureLayer1 = new ArcGISFeatureLayer(featureServiceURL1, ArcGISFeatureLayer.MODE.ONDEMAND);
        //mapView.addLayer(featureLayer0);
        mapView.addLayer(featureLayer1);

        //initialize the GraphicsLayer
        graphicsLayer = new GraphicsLayer();

        //create action points on the map
        actionPoints = createActionPoints(jsonData);
        //add actionPoints to the graphics layer
        for(int i = 0; i < actionPoints.size(); i++) {
            graphicsLayer.addGraphic(
                    new Graphic(
                            actionPoints.get(i).getLocation(),
                            new SimpleMarkerSymbol(
                                    Color.RED,
                                    10,
                                    SimpleMarkerSymbol.STYLE.CIRCLE)));
        }

        //add the graphics layer to the map
        mapView.addLayer(graphicsLayer);

        //what to do when the user taps the screen at point x,y
        mapView.setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float x, float y) {
                //location display manager can't be null for this
                //this also determines if the map is ready for user interaction
                if (locationDisplayManager != null) {
                        //for every defined point
                        for (int i = 0; i < actionPoints.size(); i++) {

                            //so we don't have to keep calc'ing references
                            final ActionPoint currentActionPoint = actionPoints.get(i);

                            //if the click is near to the current actionPoint
                            //AND the current location is near to the user click
                            if (isNearOnScreen(
                                    mapView.toScreenPoint(
                                            currentActionPoint.getLocation()),
                                    new Point(x, y),
                                    NEARBY_RADIUS_DP)
                                &&
                                isNearOnScreen(
                                        mapView.toScreenPoint(
                                                locationDisplayManager.getPoint()),
                                        new Point(x,y),
                                        NEARBY_RADIUS_DP)) {

                                //set text in "popup" to the ActionPoint's text
                                actionPointPopup.setText(currentActionPoint.getText());
                                //make the text clickable
                                actionPointPopup.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //when clicked, do the described action
                                        currentActionPoint.action();
                                    }
                                });

                                //make the button clickable, based on the currently clicked actionpointpicture
                                if (currentActionPoint instanceof ActionPointPicture) {
                                    viewPictureButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //tell the actionpointpicture to check if the picture was taken
                                            ((ActionPointPicture) currentActionPoint).checkIfPictureTaken();

                                            //if the picture is on screen now
                                            if (picOnScreen) {
                                                //take the picture off screen
                                                imageView.setVisibility(View.INVISIBLE);
                                                //change button text to indicate that it will now put the picture on screen
                                                viewPictureButton.setText("Show Picture");
                                                //indicate the picture is not on screen
                                                picOnScreen = false;
                                            }
                                            //else the picture is not on screen now
                                            else {
                                                //put the picture on screen
                                                //if the picture file exists
                                                if (new File(((ActionPointPicture) currentActionPoint).mCurrentPhotoPath).exists()) {
                                                    Log.i("TWFAGIS", "pictureTaken");
                                                    //set the imageview picture to the taken picture
                                                    Bitmap bitmap = BitmapFactory.decodeFile(((ActionPointPicture) currentActionPoint).mCurrentPhotoPath);
                                                    imageView.setImageBitmap(bitmap);
                                                    imageView.setVisibility(View.VISIBLE);
                                                    //change button text to indicate that it will now take the picture down
                                                    viewPictureButton.setText("Hide Picture");
                                                    //indicate the picture is on screen
                                                    picOnScreen = true;
                                                }
                                                //else it doesn't exist
                                                else {
                                                    //do stuff
                                                    Log.i("TWFAGIS", "pictureNotTaken");
                                                }
                                            }


                                        }
                                    });
                                    viewPictureButton.setVisibility(View.VISIBLE);
                                }
                                //else it's a normal action point
                                else {
                                    //set the view picture button invisible
                                    viewPictureButton.setVisibility(View.INVISIBLE);
                                }


                                //make the container visible
                                actionPointPopupContainer.setVisibility(View.VISIBLE);
                                //make the close button visible
                                closeWindowButton.setVisibility(View.VISIBLE);

                                Log.i("ArcGIS", "click is near point with index " + i);

                                //break from loop after action
                                i = actionPoints.size();
                            }
                        }

                }

                /*
                Toast toast = Toast.makeText(activity.getApplicationContext(), String.valueOf(mapView.toMapPoint(x, y).getX()) + " " + String.valueOf(mapView.toMapPoint(x, y).getY()), Toast.LENGTH_LONG);
                toast.show();
                */
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (locationDisplayManager != null && !locationDisplayManager.isStarted())
            locationDisplayManager.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (locationDisplayManager != null)
            locationDisplayManager.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //pause location display manager to save battery
        locationDisplayManager.pause();
    }

    @Override
    public void onStop() {
        super.onStop();
        locationDisplayManager.stop();
    }

    //returns true if the two points are within a square with side length toleranceDP of each other
    public Boolean isNearOnScreen(Point point0, Point point1, float toleranceDP) {
        //check that points are not null
        if (point0 == null || point1 == null) {
            return false;
        }
        //get pixel value of tolerance
        float tolerancePX = toleranceDP * pxPerDp;
        //if the one of the points is outside the tolerance range, return false
        if ((point0.getX() >= (point1.getX() - tolerancePX)) &&
            (point0.getX() <= (point1.getX() + tolerancePX)) &&
            (point0.getY() >= (point1.getY() - tolerancePX)) &&
            (point0.getY() <= (point1.getY() + tolerancePX)))
            return true;
        else
            return false;
    }

    public ArrayList<ActionPoint> createActionPoints(String json) {
        ArrayList<ActionPoint> newActionPoints = new ArrayList<ActionPoint>();
        //credit goes to GrlsHu on StackOverflow
        try {
            //create new JSON Object
            JSONObject jsonObject = new JSONObject(json);
            //read the action point array from the object
            JSONArray jsonArray = jsonObject.getJSONArray("actionpoints");

            //fill newActionPoints with ActionPoints from the json data
            for (int i = 0; i < jsonArray.length(); i++) {
                //create new ActionPoint using Point generated from JSON file's lat/lon pair
                JSONObject arrayObject = jsonArray.getJSONObject(i);
                //if the ActionPoint is a picture AP
                if (arrayObject.getString("type").equals("picture")) {
                    newActionPoints.add(
                            new ActionPointPicture(
                                    activity,
                                    new Point(
                                            arrayObject.getDouble("longitude"),
                                            arrayObject.getDouble("latitude")),
                                    arrayObject.getString("text"),
                                    arrayObject.getString("filename")));
                }
                else {
                    newActionPoints.add(
                            new ActionPoint(
                                    activity,
                                    new Point(
                                            arrayObject.getDouble("longitude"),
                                            arrayObject.getDouble("latitude")),
                                    arrayObject.getString("text")));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return newActionPoints;
    }

    public void centerMap(String json, MapView map) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            double lat = jsonObject.getDouble("latitude");
            double lon = jsonObject.getDouble("longitude");

            map.centerAt(new Point(lon, lat), true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

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

    /*
    public void putPicOnScreen(String path) {

        mapView.setVisibility(View.INVISIBLE);
        actionPointPopupContainer.setVisibility(View.INVISIBLE);

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        imageView.setImageBitmap(bitmap);
        Log.i("TWFAGIS", "imageView bitmap set: " + imageView.getWidth() + " " + imageView.getHeight());
    }
    */
}
