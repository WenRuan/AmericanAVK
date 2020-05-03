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
 * @file LocationErrorDialog.java
 * 
 * @brief Implementation for the page that displays the dialog for a location error
 * 
 * @details Implements everything needed to display an error dialog in case of error
 * 
 * @version 1.00
 *          1 May 2020
 *          Initial implementation of the LocationErrorDialog.java
 *
 * @note Connected with the MapsActivity
 */
 //
 // Packages ************************************************
 //
package com.tensorflow.AVK.CS426.demo;
// Imports **************************************************
// android imports 
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
// TensorFlow import
import org.tensorflow.lite.examples.demo.R;
//
// Class definition **********************************************
//
public class LocationErrorDialog extends DialogFragment {
    // 
    // Class variables *******************************************
    // 
    /// NONE
    //
    // Class functions *******************************************
    // 
    /**
    * @brief Function creates a dialog for a location error dialog
    *
    * @details if the user does not allow permissions, this dialog box will be displayed
    *
    * @pre savedInstanceState is the cache of the state of the application
    * 
    * @post A location error dialog is displayed
    *
    * @param[in] savedInstanceState
    *            holds the cached of the state of the application
    */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Location Permissions Denied
        builder.setTitle(R.string.dialog_location_error)
                .setMessage(R.string.dialog_location_message)
                .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: Nothing. This issue is now in the user's hands >:(
                    }
                });


        return builder.create();
    }
}