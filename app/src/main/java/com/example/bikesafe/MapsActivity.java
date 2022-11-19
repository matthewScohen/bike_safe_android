package com.example.bikesafe;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.bikesafe.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker at UF
        LatLng uf = new LatLng(29.65056336935438, -82.34281887899938);
        mMap.addMarker(new MarkerOptions().position(uf).title("Marker at UF"));

        //creating camera bounds
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(new LatLng(29.6504963, -82.3423021))
                .include(new LatLng(29.6448399, -82.3503985))
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));

        Polyline testLine =  mMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(29.6504963, -82.3423061),
//                        new LatLng(29.6497545, -82.3423021),

                        new LatLng(29.6497545, -82.3423021),
//                        new LatLng(29.6497543, -82.3447231),

                        new LatLng(29.6497543, -82.3447231),
//                        new LatLng(29.6487630, -82.3448427),

                        new LatLng(29.6487630, -82.3448427),
//                        new LatLng(29.6486438, -82.34972020000001),

                        new LatLng(29.6486438, -82.34972020000001),
//                        new LatLng(29.6448767, -82.3503985),

                        new LatLng(29.6448767, -82.3503985),
//                        new LatLng(29.6448399, -82.34859729999999),

                        new LatLng(29.6448399, -82.34859729999999),
                        new LatLng(29.6454873, -82.34859349999999)
                        )
        );
        testLine.setTag("TEST");
        testLine.setColor(0xff0082ff);
        testLine.setWidth(15);
    }
}