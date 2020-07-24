package com.example.notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapDialog extends AppCompatDialogFragment implements OnMapReadyCallback {
    Double latitude, longitude;
    GoogleMap map;
    MapView mapView;
    TextView locationInfo;
    Context context;
    MapDialogListener listener;
    GPSTracker gpsTracker;
    Location mLocation;

    public MapDialog(Context context, Double latitude, Double longitude) {
        this.context = context;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.map_dialog, null);
        locationInfo = view.findViewById(R.id.locationInfo);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        builder.setView(view)
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyCordinates(latitude, longitude);
                    }
                });
        return builder.create();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        gpsTracker = new GPSTracker(context);

        if (latitude == null || longitude == null) {
            mLocation = gpsTracker.getLocation();
            latitude = mLocation.getLatitude();
            longitude = mLocation.getLongitude();
            locationInfo.setText(getResources().getString(R.string.street) + ": " + gpsTracker.getAddress(latitude, longitude).get(0).getAddressLine(0));
            LatLng location = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(location).title(latitude + ", " + longitude));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        } else {
            locationInfo.setText(getResources().getString(R.string.street) + ": " + gpsTracker.getAddress(latitude, longitude).get(0).getAddressLine(0));
            LatLng location = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(location).title(latitude + ", " + longitude));
            map.moveCamera(CameraUpdateFactory.newLatLng(location));
        }
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                locationInfo.setText(getResources().getString(R.string.street) + ": " + gpsTracker.getAddress(latitude, longitude).get(0).getAddressLine(0));
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title(latitude + ", " + longitude));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MapDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement");

        }
    }

    public interface MapDialogListener {
        void applyCordinates(Double latitude, Double longitude);
    }

}