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
 * @file SettingsActivity.java
 *
 * @brief Shows the project description and website information.
 *
 */

package com.tensorflow.AVK.CS426.demo;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.preference.PreferenceFragmentCompat;

import org.tensorflow.lite.examples.demo.R;

public class SettingsActivity extends AppCompatActivity {

    String htmlText = "<h2>Scan Hydrant</h2>\n" +
            "<p>Open the 'Scan Hydrant' page. If prompted to give permissions, select 'Yes' as an option. " +
            "Place phone directly in front of the hydrant and press the 'Capture' button. If a hydrant has been successfully detected, " +
            "the detected model manual will pull up automatically if it already exists in the database. " +
            "If the object is a hydrant but is not in the database, please head to the maps page to insert the hydrant.</p>" +
            "<h2>Browse All</h2>\n" +
            "<p>The 'Browse All' allows the user to look at every model of hydrants that are available on the AVK website. If the app cannot successfully detect the hydrant model, " +
            "please use this functionality as a way to manually identify and select the hydrant that is being worked on.</p>" +
            "<h2>Hydrant Map</h2>\n" +
            "<p>To view every hydrant within the area, select the 'Hydrant Map' option. If prompted to give permissions, select 'Yes' as an option. " +
            "Your location is indicated by a tiny blue dot, and the hydrant locations are indicated by the red pin. If you have strayed too far from your original location, press the refresh " +
            "button located on the top right corner. To add in a new hydrant, select the plus icon.</p>" +
            "<h2>Website</h2>\n" +
            "<p>For more information about everything else, feel free to visit our <a href=\"https://sinouye.github.io/AnAmericanAVKAutomaton_Website/\">website</a></p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TextView htmlToTextView = findViewById(R.id.htmlToTextView);
        htmlToTextView.setText(HtmlCompat.fromHtml(htmlText, 0));
    }
}