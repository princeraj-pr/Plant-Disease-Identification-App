package com.sih23.plantdiseaseidentifiers;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;


public class LocationViewModel extends AndroidViewModel {

    private final LiveData<String[]> coordinate;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        FusedLocationProviderClient fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(application);
        DefaultLocationTracker locationTracker = new DefaultLocationTracker(
                fusedLocationClient,
                this.getApplication()
        );
        coordinate = locationTracker.getCurrentLocation();
    }

    public LiveData<String[]> getCoordinate() {
        return coordinate;
    }
}
