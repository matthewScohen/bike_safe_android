package com.example.bikesafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.bikesafe.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

public class MapsActivityTest extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_test);

        Bundle mapViewBundle = null;
        if(savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle("MapViewBundleKey");
        }
        mMap = (MapView) findViewById(R.id.mapView);
        mMap.onCreate(mapViewBundle);
        mMap.getMapAsync(this);

        Log.d("MAPTEST: ", "OnCreate");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("MAPTEST: ", "onMapReady");

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}