package com.example.cs426demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager; //For open camera permissions
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    //May need to be replaced
    //Camera/ML
    private String[] permissionsCamera = {Manifest.permission.CAMERA};
    public void openCamera(View view) {
        requestPermissions(permissionsCamera, 200); //requestCode???
        //Checks if permissions was granted, then opens camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        }
    }
    //GPS
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
