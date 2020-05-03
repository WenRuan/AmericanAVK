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
 * @file MarkerOnClickDialog.java
 * 
 * @brief Implementation for the page that displays the dialog for a chosen hydrant
 * 
 * @details Implements everything needed to display the title and description of a chosen hydrant
 * 
 * @version 1.00
 *          1 May 2020
 *          Initial implementation of the MarkerOnClickDialog.java
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
import android.content.Intent;
import android.os.Bundle;
// androidx imports
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
// TensorFlow imports
import org.tensorflow.lite.examples.demo.R;
//
// Class definition **********************************************
//
public class MarkerOnClickDialog extends DialogFragment {
    // 
    // Class variables *******************************************
    // 
    String title;
    String description;
    String link;
    MapsActivity map;
    //
    // Class functions *******************************************
    // 
    /**
    * @brief Function initializes the class
    *
    * @details All variables declared above are initialized
    *
    * @pre title, description, link, and map are uninitialized
    * 
    * @post title, description, link, and map are initialized
    *
    * @param[in] hydrantTitle
    *            Stores the title of the chosen hydrant
    *
    * @param[in] hydrantDescription
    *            Stores the description of the chosen hydrant
    * @param[in] hydrantLink
    *            Stores the link to the manual of a chosen hydrant
    *
    * @param[in] hydrantMap
    *            Stores the Maps screen needed in order to display the manual
    */
    MarkerOnClickDialog(
            String hydrantTitle,
            String hydrantDescription,
            String hydrantLink,
            MapsActivity hydrantMap
    ) {
        title = hydrantTitle;
        description = hydrantDescription;
        link = hydrantLink;
        map = hydrantMap;
    }
    /**
    * @brief Function creates a dialog to display information about a chosen hydrant
    *
    * @details A hydrant's title and description are displayed and allows user's to view
    *          manuals if they so wish
    *
    * @pre savedInstanceState is the cache of the state of the application
    * 
    * @post Information about the hydrant is displayed to the user
    *
    * @param[in] savedInstanceState
    *            holds the cached of the state of the application
    */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Location Permissions Denied
        builder.setTitle(title)
                .setMessage(description)
                .setPositiveButton(R.string.dialog_displayPDF, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openWebActivity(link);
                    }
                });


        return builder.create();
    }
    /**
    * @brief Function diplays the manual if the user wanted to see it
    *
    * @details The manual activity class is opened to be viewed by the user
    *
    * @pre the manual_link is known
    * 
    * @post The specific manual of the hydrant is displayed to the user
    *
    * @param[in] manual_link
    *            holds the link needed to be opened by the manual activity class
    */
    public void openWebActivity(String manual_link){
        Intent intent = new Intent(map, ManualActivity.class);
        intent.putExtra("MANUAL_LINK", manual_link);
        startActivity(intent);
    }
}