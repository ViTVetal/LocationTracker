package com.gocodes.locationtracker.ui.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.utils.SizeConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView ivLocation, ivSettings, ivRefresh;
    private FloatingActionButton fab;
    private Spinner spMapType;

    private GoogleMap map;

    private final static int LOCATION_PERMISSION_REQUEST = 100;

    private int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivLocation = (ImageView) findViewById(R.id.ivLocation);
        ivLocation.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_map_marker)
                .color(ContextCompat.getColor(this, R.color.dark_grey)));

        ivSettings = (ImageView) findViewById(R.id.ivSettings);
        ivSettings.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_gear)
                .color(Color.GRAY));
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);
        ivRefresh.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_refresh)
                .color(Color.GRAY));
        ivRefresh = (ImageView) findViewById(R.id.ivRefresh);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));

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

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

      //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12));
    }

    public void onClickChangeState(View v) {
        fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_pause)
                .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));
    }

    public void onClickSettings(View v) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(this);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}