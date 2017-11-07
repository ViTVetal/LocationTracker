package com.gocodes.locationtracker.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.gocodes.locationtracker.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private Spinner spMapType;

    private double latitude, longitude;
    private String date;
    private int check = 0;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);

        date = getIntent().getStringExtra("date");

        spMapType = (Spinner) findViewById(R.id.spMapType);
        spMapType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(++check > 1) {
                    switch (i) {
                        case 0:
                            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case 1:
                            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case 2:
                            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                    }
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        LatLng selectedLocation = new LatLng(latitude, longitude);

        map.addMarker(new MarkerOptions().position(selectedLocation).title(date).snippet(latitude + " " + longitude));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 12));
    }
}