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
 * @file MapSettingsActivity.java
 * 
 * @brief Implementation for the appbar of the map. Currently disabled
 * 
 * @details Implements the appbar, which has a settings button. Currently disabled
 * 
 * @version 1.00
 *          1 May 2020
 *          Initial implementation of the MapSettings.java
 *
 * @note Connected with the MapsActivity
 */
 //
 // Packages ************************************************
 //
package com.tensorflow.AVK.CS426.demo;
// Imports **************************************************
// android imports
import android.os.Bundle;
// androidx imports
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;
// TensorFlow imports
import org.tensorflow.lite.examples.demo.R;
//
// Class definition **********************************************
//
public class MapSettingsActivity extends AppCompatActivity {
    // 
    // Class variables *******************************************
    // 
    /// NONE
    //
    // Class functions *******************************************
    // 
    /**
    * @brief Function creates the action bar for the maps screen
    *
    * @details Function creates an appbar based on the settings activity xml
    *
    * @pre savedInstanceState is the cache of the state of the application
    * 
    * @post An appbar is created from the settings activity
    *
    * @param[in] savedInstanceState
    *            holds the cached of the state of the application
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

}