package com.example.cs426demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

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
