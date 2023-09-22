package com.sih23.plantdiseaseidentifiers;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class DefaultLocationTracker {

    private String LOG_TAG = DefaultLocationTracker.class.getSimpleName();

    FusedLocationProviderClient locationClient;
    Application application;

    public DefaultLocationTracker(FusedLocationProviderClient locationClient, Application application) {
        this.locationClient = locationClient;
        this.application = application;
    }

    @SuppressLint("MissingPermission")
    public MutableLiveData<String[]> getCurrentLocation() {
        boolean hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        boolean hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
                application,
                Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;

        LocationManager locationManager = (LocationManager) application.getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // Return null if permission in not granted
        if (!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            Log.d(LOG_TAG, "permission is denied");
            return null;
        }
        final MutableLiveData<String[]> currentCoordinate = new MutableLiveData<>();
        locationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    String[] cood = {
                            String.valueOf(location.getLongitude()),
                            String.valueOf(location.getLatitude())
                    };
                    currentCoordinate.setValue(cood);
                    Log.d(LOG_TAG, "onSuccess log: " + cood[0] + " lat: " + cood[1]);
                } else {
                    Log.d(LOG_TAG, "location is null");
                }
            }
        });
        return currentCoordinate;
    }
}
