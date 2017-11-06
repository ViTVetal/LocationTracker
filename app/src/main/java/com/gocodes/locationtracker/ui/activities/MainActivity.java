package com.gocodes.locationtracker.ui.activities;

import android.Manifest;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.db.LocalDB;
import com.gocodes.locationtracker.model.LocationInfo;
import com.gocodes.locationtracker.network.API;
import com.gocodes.locationtracker.network.requests.SendLocationRequest;
import com.gocodes.locationtracker.services.LocationService;
import com.gocodes.locationtracker.utils.GlobalVariables;
import com.gocodes.locationtracker.utils.LogWriter;
import com.gocodes.locationtracker.utils.SizeConverter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ImageView ivLocation, ivSettings, ivRefresh, ivClock;
    private FloatingActionButton fab;
    private Spinner spMapType;
    private TextView tvAssetId, tvLastUpdate;
    private LinearLayout llLastUpdate;

    private LocationManager locationManager;

    private GoogleMap map;

    private final static int LOCATION_PERMISSION_REQUEST = 100;

    private int check = 0;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.app_name));

        llLastUpdate = (LinearLayout) findViewById(R.id.llLastUpdate);

        tvAssetId = (TextView) findViewById(R.id.tvAssetId);
        tvLastUpdate = (TextView) findViewById(R.id.tvLastUpdate);

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
        ivClock= (ImageView) findViewById(R.id.ivClock);
        ivClock.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_clock_o)
                .color(Color.GRAY));

        fab = (FloatingActionButton) findViewById(R.id.fab);

        if(isServiceRunning(LocationService.class)) {
            fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_pause)
                    .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));
        } else {
            fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                    .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        updateLastLocationDate();
                    }
                }, new IntentFilter(LocationService.ACTION_LOCATION_UPDATED)
        );

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

        realm = Realm.getDefaultInstance();

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};

                    ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST);


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
    protected void onResume() {
        super.onResume();

        tvAssetId.setText(GlobalVariables.getAssetId(this));

        updateLastLocationDate();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);

        Location location = getLastKnownLocation();
        Log.d("myLogs", location + " loc");
        if (location != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 12));
        }

      //  googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLocation, 12));
    }

    public void onClickChangeState(View v) {
        Intent intent = new Intent(this, LocationService.class);
        if(isServiceRunning(LocationService.class)) {
            stopService(intent);

            fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_play)
                    .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));
        } else {
            startService(intent);

            fab.setImageDrawable(new IconDrawable(this, FontAwesomeIcons.fa_pause)
                    .color(Color.WHITE).sizeDp(SizeConverter.dpToPx(24, this)));
        }
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

    public void onClickUpdateLocation(View view) {
        Location location = getLastKnownLocation();
        Log.d("myLogs", location + " loc");
        if (location != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", GlobalVariables.getEmail(this));
            params.put("password", GlobalVariables.getPassword(this));
            params.put("assetId", GlobalVariables.getAssetId(this).replaceAll("-", ""));
            params.put("latitude", String.valueOf(location.getLatitude()));
            params.put("longitude", String.valueOf(location.getLongitude()));
            params.put("enableHistory", String.valueOf(GlobalVariables.isUpdateHistory(this)));
            params.put("customValue1", "934");

            JSONObject param = new JSONObject(params);

            realm.beginTransaction();
            final LocationInfo locationInfo = realm.createObject(LocationInfo.class);
            locationInfo.setLatitude(location.getLatitude());
            locationInfo.setLongitude(location.getLongitude());
            locationInfo.setManually(true);
            locationInfo.setDate(Calendar.getInstance().getTimeInMillis());

            SendLocationRequest jsonObjectRequest = new SendLocationRequest(param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("myLogs", "response :"+response);
                            locationInfo.setSuccess(true);

                            realm.commitTransaction();

                            updateLastLocationDate();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    locationInfo.setSuccess(false);

                    realm.commitTransaction();

                    updateLastLocationDate();
                    Log.d("myLogs", "Error: " + error.toString());
                }
            }){

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };

            API.getInstance(this).addToRequestQueue(jsonObjectRequest);
        }
    }

    private Location getLastKnownLocation() {
        LocationManager locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void updateLastLocationDate() {
        LocationInfo lastLocationInfo = realm.where(LocationInfo.class).findAll().last();

        if(lastLocationInfo != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(lastLocationInfo.getDate());
            String date = DateFormat.format("dd-MM-yyyy HH:mm:ss", cal).toString();
            tvLastUpdate.setText(date);

            llLastUpdate.setVisibility(View.VISIBLE);
        } else {
            llLastUpdate.setVisibility(View.GONE);
        }
    }

    private boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}