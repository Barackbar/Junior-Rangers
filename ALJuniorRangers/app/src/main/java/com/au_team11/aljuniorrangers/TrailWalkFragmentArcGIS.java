package com.au_team11.aljuniorrangers;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.LocationDisplayManager;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.popup.PopupContainer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.table.TableException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
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

    //main view
    View view;


    //the map on screen
    MapView mapView;
    //controls displaying the current device location
    LocationDisplayManager locationDisplayManager;
    //how the map is drawn
    SpatialReference spatialReference;

    //file that holds trail data
    Geodatabase geodatabase;
    //table that holds features
    GeodatabaseFeatureTable geodatabaseFeatureTable;
    //layer that holds the trail graphics
    GraphicsLayer trailGraphicsLayer;

    //local basemap
    ArcGISLocalTiledLayer basemap;
    //filename
    String basemapFileName = "sanfrancisco.tpk";

    //trail walk data filename
    String fileName;
    //Points defined by the JSON file
    ArrayList<ActionPoint> actionPoints;
    //layer that holds the trail point graphics
    GraphicsLayer trailPointGraphicsLayer;


    //views for the info view (iV) aspect
    //textview where "popup" will dump its info
    TextView iV_TextView;
    //the above textview container
    ScrollView iV_ScrollView;
    //button used to close the popup
    Button iV_CloseButton;
    //button used for actionpointpictures to view the picture, if taken already
    Button iV_PreviewPicButton;
    //place to put the picture that was taken
    ImageView iV_PreviewPicImageView;
    //whether the picture is on screen or not
    Boolean iV_PicOnScreen;

    //views for the view all pics (vAP) aspect
    //the linear layout that holds everything and has a translucent background
    LinearLayout vAP_LinearLayout;
    //button to activate the vAP screen
    Button vAP_ViewButton;
    //button to cycle through the pictures in positive order
    Button vAP_NextButton;
    //button to cycle through the pictures in negative order
    Button vAP_PrevButton;
    //button to exit the vAP screen
    Button vAP_ExitButton;
    //where the picture will be displayed
    ImageView vAP_ImageView;
    //the index in actionPoints of the last point looked at
    int vAP_CurrentIndex;


    //used to test REST data requests
    //String featureServiceURL0 = "https://conservationgis.alabama.gov/adcnrweb/rest/services/Trails_SLD/MapServer/2";
    //String featureServiceURL1 = "https://conservationgis.alabama.gov/adcnrweb/rest/services/Trails_SLD/MapServer/1";
    //ArcGISFeatureLayer featureLayer0;
    //ArcGISFeatureLayer featureLayer1;


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


        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putInt("TRAIL", 25);
        editor.commit();


        //get textview from layout
        iV_TextView = (TextView) view.findViewById(R.id.ActionButtonPopup);
        //get the textview's containing scrollview from layout
        iV_ScrollView = (ScrollView) view.findViewById(R.id.ActionButtonPopupContainer);
        //get the info view close button from layout
        iV_CloseButton = (Button) view.findViewById(R.id.CloseWindowButton);
        //get the preview picture button from layout
        iV_PreviewPicButton = (Button) view.findViewById(R.id.ViewPictureButton);
        //get the preview picture image view from layout
        iV_PreviewPicImageView = (ImageView) view.findViewById(R.id.previewImageView);
        //indicate that there is no preview picture on screen
        iV_PicOnScreen = false;

        //set the close button's on click listener
        iV_CloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //since picture is off screen, needs to set text to indicate it can be put on screen
                iV_PreviewPicButton.setText("View Picture");
                iV_PicOnScreen = false;

                //hide the info view
                hideInfoView();
            }
        });


        //get the view all pics background linear layout from layout
        vAP_LinearLayout = (LinearLayout) view.findViewById(R.id.picViewLinearLayout);
        //get the view all pics button from layout
        vAP_ViewButton = (Button) view.findViewById(R.id.viewAllPicsButton);
        //get the view all pics next button from layout
        vAP_NextButton = (Button) view.findViewById(R.id.nextPicButton);
        //get the view all pics previous button from layout
        vAP_PrevButton = (Button) view.findViewById(R.id.prevPicButton);
        //get the view all pics exit button from layout
        vAP_ExitButton = (Button) view.findViewById(R.id.exitViewAllButton);
        //get the view all pics image view from layout
        vAP_ImageView = (ImageView) view.findViewById(R.id.viewAllImageView);
        //indicate that the view all pics cycle should start at action point 0
        vAP_CurrentIndex = 0;

        //set the view all pics button on click listener
        vAP_ViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show the view all pics views
                showViewAllPicsView();
            }
        });

        //set the view all pics next button on click listener
        vAP_NextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //number of points checked, will stop infinite loop
                int numPointsChecked = 0;
                //index of the next point to check
                int i = (vAP_CurrentIndex + 1) % actionPoints.size();
                //loop through actionpoints, starting with current index
                while (numPointsChecked < actionPoints.size()) {
                    //if this is a picture point
                    ActionPoint currentActionPoint = actionPoints.get(i);
                    if (currentActionPoint instanceof ActionPointPicture) {
                        //refresh its decision if the picture is taken
                        ((ActionPointPicture) currentActionPoint).checkIfPictureTaken();
                        //if its picture has been taken
                        if (((ActionPointPicture) currentActionPoint).pictureTaken) {
                            //put the image into the imageview
                            Bitmap bitmap = BitmapFactory.decodeFile(((ActionPointPicture) currentActionPoint).pathToFile);
                            vAP_ImageView.setImageBitmap(bitmap);
                            //break from the loop
                            numPointsChecked = actionPoints.size();
                            //set current index to this point
                            vAP_CurrentIndex = i;
                        }
                    }
                    Log.i("nextButton", "" + numPointsChecked);
                    //indicate we've checked another point
                    numPointsChecked++;
                    //if we've just checked the point at the end of the action point list
                    if (i == actionPoints.size() - 1) {
                        //loop around to the beginning of the list
                        i = 0;
                    } else {
                        //check next point
                        i++;
                    }
                }
            }
        });

        //set the view all pics previous button on click listener
        vAP_PrevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //number of points checked, will stop infinite loop
                int numPointsChecked = 0;
                //index of the next point to check
                //NOTE: -n % (k) == -n (where k > n) for some reason,
                //thus need another way to get index below current one
                //Solution: (n + (k-1)) % (k) ==
                int i = (vAP_CurrentIndex + (actionPoints.size() - 1)) % actionPoints.size();
                Log.i("i: ", "" + i);
                //loop through actionpoints, starting with current index
                while (numPointsChecked < actionPoints.size()) {
                    //if this is a picture point
                    ActionPoint currentActionPoint = actionPoints.get(i);
                    if (currentActionPoint instanceof ActionPointPicture) {
                        //if its picture has been taken
                        if (((ActionPointPicture) currentActionPoint).pictureTaken) {
                            //put the image into the imageview
                            Bitmap bitmap = BitmapFactory.decodeFile(((ActionPointPicture) currentActionPoint).pathToFile);
                            vAP_ImageView.setImageBitmap(bitmap);
                            //break from the loop
                            numPointsChecked = actionPoints.size();
                            //set current index to the next point
                            vAP_CurrentIndex = i;
                        }
                    }
                    Log.i("prevButton", "" + numPointsChecked);
                    //indicate we've checked another point
                    numPointsChecked++;
                    //if we've just checked the point at the beginning of the action point list
                    if (i == 0) {
                        //loop around to the end of the list
                        i = actionPoints.size() - 1;
                    } else {
                        //check next point down
                        i--;
                    }
                }
            }
        });

        //set the view all pics exit button on click listener
        vAP_ExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide the view all pics views
                hideViewAllPicsView();
            }
        });

        //hide the info view at start
        hideInfoView();

        //hide the view all pics view at start
        hideViewAllPicsView();


        //get json file to use
        fileName = getArguments().getString(getResources().getString(R.string.AssetBundleKey));
        //load json into a string
        final String jsonData = loadJSONFromAsset(fileName);

        //initialize the GraphicsLayer
        trailPointGraphicsLayer = new GraphicsLayer();
        //create action points
        actionPoints = createActionPoints(jsonData);
        //add actionPoints to the graphics layer
        for(int i = 0; i < actionPoints.size(); i++) {
            trailPointGraphicsLayer.addGraphic(
                    new Graphic(
                            actionPoints.get(i).getLocation(),
                            new SimpleMarkerSymbol(
                                    Color.RED,
                                    10,
                                    SimpleMarkerSymbol.STYLE.CIRCLE)));
        }


        //get reference to the map
        mapView = (MapView) view.findViewById(R.id.map);
        //mapview initialization must occur only after the map is ready
        mapView.setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (o == mapView && status == STATUS.INITIALIZED) {
                    //get the spatial reference of the map to sync with layers
                    spatialReference = mapView.getSpatialReference();
                    Log.i("SpatialReference", spatialReference.getText());
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


        //get the geodatabase
        basemap = new ArcGISLocalTiledLayer(context.getExternalFilesDir(null).getAbsolutePath() + "/" + basemapFileName);
        //put the basemap on the map
        mapView.addLayer(basemap);

        try {
            geodatabase = new Geodatabase(context.getExternalFilesDir(null).getAbsolutePath() + "/servicesdata.geodatabase");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //get the trails
        geodatabaseFeatureTable = geodatabase.getGeodatabaseTables().get(0);
        try {
            Log.i("TWFAGIS num tables", "" + geodatabase.getGeodatabaseTables().size());

            trailGraphicsLayer = new GraphicsLayer();

            for (long i = 0; i < 123; i++) {
                if (geodatabaseFeatureTable.checkFeatureExists(i)) {
                    Log.i("gdb geometry type", geodatabaseFeatureTable.getFeature(i).getGeometry().getType().toString());
                    trailGraphicsLayer.addGraphic(new Graphic(geodatabaseFeatureTable.getFeature(i).getGeometry(), new SimpleLineSymbol(Color.BLACK, 4)));
                    Log.i("TWFAGIS feature exists", "" + i);
                }
            }
            mapView.addLayer(trailGraphicsLayer);
        }
        catch (TableException e) {
            e.printStackTrace();
        }

        //get the boundaries
        geodatabaseFeatureTable = geodatabase.getGeodatabaseTables().get(1);
        try {
            Log.i("TWFAGIS num tables", "" + geodatabase.getGeodatabaseTables().size());

            trailGraphicsLayer = new GraphicsLayer();

            for (long i = 0; i < 150; i++) {
                if (geodatabaseFeatureTable.checkFeatureExists(i)) {
                    Log.i("gdb geometry type", geodatabaseFeatureTable.getFeature(i).getGeometry().getType().toString());
                    trailGraphicsLayer.addGraphic(new Graphic(geodatabaseFeatureTable.getFeature(i).getGeometry(), new SimpleLineSymbol(Color.BLUE, 4)));
                    Log.i("TWFAGIS feature exists", "" + i);
                }
            }
            mapView.addLayer(trailGraphicsLayer);
        }
        catch (TableException e) {
            e.printStackTrace();
        }


        //add the trail points graphics layer to the map
        mapView.addLayer(trailPointGraphicsLayer);


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

                            //if the click is near to the current actionPoint AND the current location is near to the user click
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

                                //set text in info view to the ActionPoint's text
                                iV_TextView.setText(currentActionPoint.getText());
                                //make the text clickable
                                iV_TextView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        //when clicked, do the described action
                                        currentActionPoint.action();
                                    }
                                });

                                //make the button clickable, based on the currently clicked actionpointpicture
                                if (currentActionPoint instanceof ActionPointPicture) {
                                    iV_PreviewPicButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            //tell the actionpointpicture to check if the picture was taken
                                            ((ActionPointPicture) currentActionPoint).checkIfPictureTaken();

                                            //if the picture is on screen now
                                            if (iV_PicOnScreen) {
                                                //take the picture off screen
                                                iV_PreviewPicImageView.setVisibility(View.INVISIBLE);
                                                //change button text to indicate that it will now put the picture on screen
                                                iV_PreviewPicButton.setText("Show Picture");
                                                //indicate the picture is not on screen
                                                iV_PicOnScreen = false;
                                            }
                                            //else the picture is not on screen now
                                            else {
                                                //if the picture file exists
                                                if (new File(((ActionPointPicture) currentActionPoint).pathToFile).exists()) {
                                                    Log.i("TWFAGIS", "pictureTaken");
                                                    //set the imageview picture to the taken picture
                                                    Bitmap bitmap = BitmapFactory.decodeFile(((ActionPointPicture) currentActionPoint).pathToFile);
                                                    iV_PreviewPicImageView.setImageBitmap(bitmap);
                                                    iV_PreviewPicImageView.setVisibility(View.VISIBLE);
                                                    //change button text to indicate that it will now take the picture down
                                                    iV_PreviewPicButton.setText("Hide Picture");
                                                    //indicate the picture is on screen
                                                    iV_PicOnScreen = true;
                                                }
                                                //else it doesn't exist
                                                else {
                                                    //do stuff
                                                    Log.i("TWFAGIS", "pictureNotTaken");
                                                }
                                            }

                                        }
                                    });
                                    iV_PreviewPicButton.setVisibility(View.VISIBLE);
                                }
                                //else it's a normal action point (non picture)
                                else {
                                    //set the view picture button invisible
                                    iV_PreviewPicButton.setVisibility(View.INVISIBLE);
                                }

                                //show the info view
                                showInfoView();

                                //break from loop after action
                                i = actionPoints.size();
                            }
                        }

                }
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

    private void showInfoView() {
        //hide the view all pics button
        vAP_ViewButton.setVisibility(View.INVISIBLE);
        //show everything associated with the info view
        iV_TextView.setVisibility(View.VISIBLE);
        iV_ScrollView.setVisibility(View.VISIBLE);
        iV_CloseButton.setVisibility(View.VISIBLE);
    }

    private void hideInfoView() {
        //show the view all pics button
        vAP_ViewButton.setVisibility(View.VISIBLE);
        //hide everything associated with the info view
        iV_TextView.setVisibility(View.INVISIBLE);
        iV_ScrollView.setVisibility(View.INVISIBLE);
        iV_CloseButton.setVisibility(View.INVISIBLE);
        //might already be invisible, won't matter in that case
        iV_PreviewPicImageView.setVisibility(View.INVISIBLE);
    }

    private void showViewAllPicsView() {
        vAP_ViewButton.setVisibility(View.INVISIBLE);
        vAP_LinearLayout.setVisibility(View.VISIBLE);
        vAP_ImageView.setVisibility(View.VISIBLE);
        vAP_NextButton.setVisibility(View.VISIBLE);
        vAP_PrevButton.setVisibility(View.VISIBLE);
        vAP_ExitButton.setVisibility(View.VISIBLE);
    }

    private void hideViewAllPicsView() {
        vAP_ViewButton.setVisibility(View.VISIBLE);
        vAP_LinearLayout.setVisibility(View.INVISIBLE);
        vAP_ImageView.setVisibility(View.INVISIBLE);
        vAP_NextButton.setVisibility(View.INVISIBLE);
        vAP_PrevButton.setVisibility(View.INVISIBLE);
        vAP_ExitButton.setVisibility(View.INVISIBLE);
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
}
