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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.tensorflow.lite.examples.demo.R;

public class MarkerOnClickDialog extends DialogFragment {
    String title;
    String description;
    String link;
    MapsActivity map;

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

    public void openWebActivity(String manual_link){
        Intent intent = new Intent(map, ManualActivity.class);
        intent.putExtra("MANUAL_LINK", manual_link);
        startActivity(intent);
    }
}