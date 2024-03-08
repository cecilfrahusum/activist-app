package com.example.activist_app;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionsHandler {

    // Is this class even used anymore?? Delete if not.

    public void requestLocationPermission(Activity activity, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
    }

    // TODO: this only checks the manifest. need to change it.
    public boolean hasLocationPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestMicPermission(Activity activity, int requestCode) {
        // implement later
    }

    public boolean hasMicPermission(Activity activity) {
        return false; // implement later
    }
}
