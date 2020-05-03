/*
 * Copyright 2020 CS426 AVK Automation Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @file MapsActivity.java
 * 
 * @brief Implementation for the page that displays the Map
 * 
 * @details Implements everything needed to display a map of fire hydrants on 
 *          the American AVK Automation application
 * 
 * @version 1.00
 *          1 May 2020
 *          Initial implementation of the MapsActivity.java
 *
 * @note Connected with the main menu
 */
 //
 // Packages ************************************************
 //
package com.tensorflow.AVK.CS426.demo;
// Imports **************************************************
// androidx imports 
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
// android imports 
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
// com.google.android imports 
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
// TensorFlow import
import org.tensorflow.lite.examples.demo.R;
// Java imports 
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
//
// Class definition **********************************************
//
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    // 
    // Class variables *******************************************
    // 
    // STATUS CODES
    static final int PERMISSION_LOCATION_REQUEST_CODE = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final String REQUESTING_LOCATION_UPDATES_KEY = "LocationUpdatesBoolean";
    //
    // State checks
    boolean canUpdateLocation = false;
    //
    // Stuff for maps
    private GoogleMap mMap;
    //
    // Stuff for floating action button
    FloatingActionButton floatingButton1;
    FloatingActionButton floatingButton2;
    //
    // Tracking user location
    private LatLng mUserLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CircleOptions drawCircle;
    private Circle mapCircle;
    static final int CIRCLE_FILL_COLOR = Color.argb(255, 89, 142, 255);

    //
    // Class functions *******************************************
    // 
    /**
    * @brief Function is the initializer of the class
    *
    * @details Function runs on the creation of the activity and sets up
    *          everything needed by the hydrant map
    *
    * @pre savedInstanceState is the cache of user's last location
    * 
    * @pre All class variables besides CIRCLE_FILL_COLOR, Status codes, and statechecks
    *      are uninitialized
    *
    * @post All variables are initialized
    * 
    * @post Map is created and displayed
    *
    * @param[in] savedInstanceState
    *            holds the cached of whether location services are permissed
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize userLocation until it can be later updated
        mUserLocation = new LatLng(39.5442, -119.8164);

        // Set up user tracking stuff
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult == null) {
                    return;
                }

                for(Location location : locationResult.getLocations()) {
                    // TODO: Update UI
                    mUserLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    updateUi();
                }
            }
        };
        // Check permissions
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )) {
                // Then try to request permission again
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_LOCATION_REQUEST_CODE
                );
            }
        } else {
            canUpdateLocation = true;
        }

        //Refresh Button
        floatingButton1 = findViewById(R.id.floatingActionButton1);

        floatingButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                onRefresh();
            }
        });

        //Browse Button
        floatingButton2 = findViewById(R.id.floatingActionButton2);

        floatingButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                browseScreen(view);
            }
        });

        updateValuesFromBundle(savedInstanceState);
    }
    /**
    * @brief Function allows the browse screen to be displayed
    *
    * @details Function opens a new activity for the browse screen activity
    *
    * @pre View allows access to XML files
    *
    * @pre Runs when the add button is pressed
    *
    * @post Browse All screen is displayed
    *
    * @param[in] view
    *            holds the ability to access XML files
    */
    public void browseScreen(View view) {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }
    /**
    * @brief Function creates a menu for the maps
    *
    * @details Function creates options menu, or an appbar in menu
    *          It is currently disabled
    *
    * @pre menu stores the XML file for the maps menu
    * 
    * @pre Menu isn't inflated
    *
    * @post Menu is inflated
    * 
    * @post Map should have an appbar
    *
    * @param[in] menu
    *            holds the ability to access the map menu xml file
    *
    * @param[out] returns a boolean to show the menu
    */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    /**
    * @brief Function implements the pressing of the settings button on appbar
    *
    * @details Function opens menu settings when the settings button on appbar
    *          is pressed. Currently disabled.
    *
    * @pre MenuItem is the settings button, as that is the only button available
    *
    * @post The map settings screen is displayed
    *
    * @param[in] item
    *            holds information about which MenuItem was tapped
    *
    * @param[out] boolean
    *             holds whether the selection function was ran properly
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.map_settings:
                Intent intent = new Intent(this, MapSettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
    * @brief Function runs after request permissions is returned
    *
    * @details Function will react to whether the user agreed or disagreed to the 
    *          location services prompt
    *
    * @pre the user has responded to location permissions
    * 
    * @pre Some request code is known
    *
    * @pre permissions allowed is known
    *
    * @pre granted permissions is known
    *
    * @post Updates user location based on whether location services is enabled or
    *       returns a location error dialog
    *
    * @param[in] requestCode
    *            STATUSCODE should be 100 if success
    * 
    * @param[in] permissions
    *            stores a nonnull array of strings with allowed permissions
    *
    * @param[in] grantResults
    *            stores the result of checking what permissions are allowed
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationProviderClient.getLastLocation()
                            .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if(location != null) {
                                        mUserLocation = new LatLng(
                                                location.getLatitude(),
                                                location.getLongitude()
                                        );
                                        updateUi();
                                    }
                                    canUpdateLocation = true;
                                }
                            });
                } else {
                    DialogFragment dialog = new LocationErrorDialog();
                    dialog.show(getSupportFragmentManager(), "LocationErrorDialog");
                }
                return;
            }
        }
    }
    /**
    * @brief Function is the creator of the map
    *
    * @details Function runs on the map being ready to be displayed
    *
    * @pre Map is ready to be displayed
    *
    * @post hydrant list from database created
    * 
    * @post markers for each hydrant is displayed
    * 
    * @post The user's location is still not known
    *
    * @post marker's have listeners attached
    *
    * @param[in] googleMap
    *            holds the GoogleMap that is displayed
    */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Marker> marker = new ArrayList<Marker>();

        //Opens database file for latitude and longitude values
        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();

        //Arrays to hold values
        ArrayList<Double> latitudeList = new ArrayList<>();
        ArrayList<Double> longitudeList = new ArrayList<>();
        ArrayList<String> hydrantList = new ArrayList<>();
        latitudeList = db.getLatitude();
        longitudeList = db.getLongitude();
        hydrantList = db.getHydrants();


        //Loop through all hydrants in database and add them to the map
        LatLng[] hydrantLocation = new LatLng[100];
        for(int index = 0; index < latitudeList.size(); index++){
            hydrantLocation[index] = new LatLng(longitudeList.get(index), latitudeList.get(index));
            /**BTW Our latitude and longitude in the database is swapped**/
        }

        db.close();

        //Puts markets of hydrants all over the map
        mMap.setOnMarkerClickListener(this);
        for(int index = 0; index < hydrantList.size(); index++)
        {
            //adding into a list of markers to add onClicks
            marker.add(mMap.addMarker(new MarkerOptions().position(hydrantLocation[index]).title(hydrantList.get(index))));
        }

        drawCircle = new CircleOptions()
                .center(mUserLocation)
                .radius(10)
                .fillColor(CIRCLE_FILL_COLOR)
                .strokeColor(Color.WHITE);


        mapCircle = mMap.addCircle(drawCircle);

        mMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserLocation));
    }
    /**
    * @brief Resumes location updates
    *
    * @details Function allows user location to be actively tracked
    *
    * @pre canUpdateLocation is true in order to make location requests
    *
    * @post Location requests created
    */
    @Override
    protected void onResume() {
        super.onResume();
        if(canUpdateLocation) {
            createLocationRequest();
        }
    }
    /**
    * @brief Function pauses location updates
    *
    * @details Function stops user tracking
    *
    * @pre paused location updates
    */
    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
    /**
    * @brief Function is stops location updates
    *
    * @details Removes all queued up location updates
    *
    * @pre onPause just ran
    *
    * @post Location update queue is cleared
    */
    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    /**
    * @brief Function saves information to a cache
    *
    * @details Function stores permissions to cache
    *
    * @pre outState and outPersistentState are known
    *
    * @post User's location permissions are stored to cache
    * 
    * @param[in] outState
    *            holds the state that will be changed to whether update location is allowed
    *
    * @param[in] outPersistentState
    *            holds the constants of each state
    */
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putBoolean(
                REQUESTING_LOCATION_UPDATES_KEY,
                canUpdateLocation
        );
        super.onSaveInstanceState(outState, outPersistentState);
    }
    /**
    * @brief Function does the realtime user tracking
    *
    * @details Function accesses phone's GPS in order to figure out where the user
    *          currently is located
    *
    * @pre a location request was posted
    * 
    * @post Location request is created and handles failure to access GPS
    */
    private void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // TODO: Initialize location requests
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(
                                MapsActivity.this,
                                REQUEST_CHECK_SETTINGS
                        );
                    } catch (IntentSender.SendIntentException sendEx) {

                    }
                }
            }
        });

        // Add the request
        mFusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
        );
    }
    /**
    * @brief Function sees what is stored in cache about permissions
    *
    * @details Function returns whether the user gave location permissions and updates ui
    * 
    * @pre SavedInstanceState was saved previously
    *
    * @post UI is updated and canUpdateLocation is updated regularly
    *
    * @param[in] savedInstanceState
    *            holds the cached of location permissions
    */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if(savedInstanceState == null) {
            return;
        }
        if(savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
            canUpdateLocation = savedInstanceState.getBoolean(
                    REQUESTING_LOCATION_UPDATES_KEY
            );
        }

        updateUi();
    }
    /**
    * @brief Function updates UI
    *
    * @details Function displays a circle where the user is currently located
    *
    * @pre user's location is known
    *
    * @post A circle is drawn where a user is currently located
    * 
    * @post All previous circles are removed
    */
    void updateUi () {
        drawCircle = new CircleOptions()
                .center(mUserLocation)
                .radius(10)
                .fillColor(CIRCLE_FILL_COLOR)
                .strokeColor(Color.WHITE);
        if(mapCircle != null) {
            mapCircle.remove();
            mapCircle = mMap.addCircle(drawCircle);
        }
    }
    /**
    * @brief Function runs when a marker is clicked
    *
    * @details Function displays a onclick dialog to see if the user wants to see
    *          the manual of the chosen hydrant
    *
    * @pre marker is the tapped marker and is known
    * 
    * @pre All markers have information in the database
    *
    * @post A dialog to see the manual is displayed
    *
    * @param[in] marker
    *            holds the tapped marker
    *
    * @param[out] Returns the success of the marker information being in the database
    */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("testing", "Its working sort of");
        ArrayList<String> hydrantList = new ArrayList<>();
        DatabaseAccess db = DatabaseAccess.getInstance(getApplicationContext());
        db.open();
        hydrantList = db.getHydrants();
        for(int index = 0; index < hydrantList.size(); index++)
        {
            Log.d("test", marker.getTitle() + " Marker");
            Log.d("test", hydrantList.get(index) + " " + index);
            if(marker.getTitle().equals(hydrantList.get(index)))
            {
                //Get description of hydrant
                String desc = db.getDescription().get(index);

                //Get link of that hydrant
                String link = db.getLink(hydrantList.get(index));
                DialogFragment dialog = new MarkerOnClickDialog(
                        marker.getTitle(),
                        desc,
                        link,
                        this
                );
                dialog.show(getSupportFragmentManager(), "MarkerOnClickDialog");
                db.close();
                return true;
            }
        }
        db.close();
        return false;
    }
    /**
    * @brief Function moves camera to the user location
    *
    * @details Function runs only if the refresh button is tapped
    *
    * @pre The refresh button is tapped
    *
    * @post The map camera is focused on the user's location
    */
    public void onRefresh() {
        mMap.animateCamera(CameraUpdateFactory.newLatLng(mUserLocation));
    }
}