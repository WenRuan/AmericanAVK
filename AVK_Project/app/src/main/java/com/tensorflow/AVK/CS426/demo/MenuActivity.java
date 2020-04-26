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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager; //For open camera permissions
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import org.tensorflow.lite.examples.demo.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    //May need to be replaced
    //Camera/ML
    private String[] permissionsCamera = {Manifest.permission.CAMERA};
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void openCamera(View view) {
        requestPermissions(permissionsCamera, 200); //requestCode???
        //Checks if permissions was granted, then opens camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, DetectorActivity.class);
            startActivity(intent);
        }
    }
    //GPS
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void mapsScreen(View view) {
        String[] permissionsMap = {Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissionsMap, 200);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }
    //Browse All
    public void browseScreen(View view) {
        Intent intent = new Intent(this, BrowseActivity.class);
        startActivity(intent);
    }
    //History
    //
    //Settings
    public void settingsScreen(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
