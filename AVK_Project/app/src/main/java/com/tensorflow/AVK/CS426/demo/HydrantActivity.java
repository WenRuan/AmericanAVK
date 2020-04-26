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

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import org.tensorflow.lite.examples.demo.R;

public class HydrantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hydrant);

        String pdf = "http://www.americanavk.com/files/SS_S24XX_1214.pdf";
        WebView browser = (WebView) findViewById(R.id.webView1);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdf);
        browser.setVerticalScrollBarEnabled(true);
        browser.setHorizontalScrollBarEnabled(false);
    }
}
