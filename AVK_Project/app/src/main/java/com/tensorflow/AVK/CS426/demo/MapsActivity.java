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

package com.tensorflow.AVK.CS426.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

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

import org.tensorflow.lite.examples.demo.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    // STATUS CODES
    static final int PERMISSION_LOCATION_REQUEST_CODE = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final String REQUESTING_LOCATION_UPDATES_KEY = "LocationUpdatesBoolean";

    // State checks
    boolean canUpdateLocation = false;

    // Stuff for maps
    private GoogleMap mMap;

    // Tracking user location
    private LatLng mUserLocation;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private CircleOptions drawCircle;
    private Circle mapCircle;
    static final int CIRCLE_FILL_COLOR = Color.argb(255, 89, 142, 255);

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

        updateValuesFromBundle(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

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
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
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

    @Override
    protected void onResume() {
        super.onResume();
        if(canUpdateLocation) {
            createLocationRequest();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        outState.putBoolean(
                REQUESTING_LOCATION_UPDATES_KEY,
                canUpdateLocation
        );
        super.onSaveInstanceState(outState, outPersistentState);
    }

    // Make a location request at regular intervals to update
    // users locations...
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
                Log.d("test", "222222222222222222222");
                //Get link of that hydrant
                String link = db.getLink(hydrantList.get(index));
                openWebActivity(link);
                db.close();
                return true;
            }
        }
        db.close();
        return false;
    }

    public void openWebActivity(String manual_link){
        Intent intent = new Intent(this, ManualActivity.class);
        intent.putExtra("MANUAL_LINK", manual_link);
        startActivity(intent);
    }
}
