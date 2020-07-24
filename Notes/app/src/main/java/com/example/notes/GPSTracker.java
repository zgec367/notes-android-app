package com.example.notes;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import java.util.List;

public class GPSTracker extends Service implements LocationListener {

    private final Context context;
    protected LocationManager locationManager;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    Location location;
    List<Address> addresses = null;

    public GPSTracker(Context context) {
        this.context = context;
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        }
                    }
                }
                if (location == null) {
                    if (isNetworkEnabled) {
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return location;
    }

    public List<Address> getAddress(Double latitude, Double longitude) {
        try {
            Geocoder geocoder = new Geocoder(context);
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (Exception ex) {

        }
        return addresses;
    }

    // followings are the default method if we imlement LocationListener //
    public void onLocationChanged(Location location) {

    }

    public void onStatusChanged(String Provider, int status, Bundle extras) {

    }

    public void onProviderEnabled(String Provider) {

    }

    public void onProviderDisabled(String Provider) {

    }

    public IBinder onBind(Intent arg0) {
        return null;
    }

}
