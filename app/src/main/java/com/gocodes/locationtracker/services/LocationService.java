package com.gocodes.locationtracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gocodes.locationtracker.model.LocationInfo;
import com.gocodes.locationtracker.network.API;
import com.gocodes.locationtracker.network.requests.SendLocationRequest;
import com.gocodes.locationtracker.utils.GlobalVariables;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.realm.Realm;

public class LocationService extends Service {
    private static final String TAG = "tracker_service";
    private LocationManager mLocationManager = null;

    FusedLocationProviderClient fusedLocationClient;

    private static final float LOCATION_MIN_DISTANCE = 10f;
    private static final int SEC = 1000;
    private static final int MIN = 60 * SEC;
    private static final int HOUR = 60 * MIN;
    private static final int REAL_TIME_INTERVAL = 1 * MIN;

    public static final String ACTION_LOCATION_UPDATED = "location_updated";

    private Realm realm;

    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult); // why? this. is. retarded. Android.
            Location currentLocation = locationResult.getLastLocation();

            Log.d("myLogs", "update " + currentLocation);

            sendLocationUpdates(currentLocation);
        }
    };

    private class TimeLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            sendLocationUpdates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    private class RealTimeLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            sendLocationUpdates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    TimeLocationListener timeLocationListener = new TimeLocationListener();

    RealTimeLocationListener realTimeLocationListener = new RealTimeLocationListener();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
       // initializeLocationManager();

        realm = Realm.getDefaultInstance();

//        int byTimeInterval = Integer.valueOf(GlobalVariables.FREQUENCIES[GlobalVariables.getFrequencyIndex(this)]);
//        byTimeInterval = 1000 * 60 * 60 * byTimeInterval;
//
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//
//        float minDistance = 0;
//        if(GlobalVariables.isUpdateOnMove(this))
//            minDistance = LOCATION_MIN_DISTANCE;

        /*if(GlobalVariables.isRealTimeUpdate(this)) {
            try {
                mLocationManager.requestLocationUpdates(
                        REAL_TIME_INTERVAL, minDistance, criteria,
                        realTimeLocationListener, null);
            } catch (java.lang.SecurityException ex) {

            } catch (IllegalArgumentException ex) {

            }
        } else {
            try {
                mLocationManager.requestLocationUpdates(
                        byTimeInterval, minDistance, criteria,
                        timeLocationListener, null);
            } catch (java.lang.SecurityException ex) {

            } catch (IllegalArgumentException ex) {

            }
        } */

        LocationRequest locationRequest = new LocationRequest();
        if(GlobalVariables.isRealTimeUpdate(this)) {
            locationRequest.setInterval(REAL_TIME_INTERVAL);
            locationRequest.setFastestInterval(REAL_TIME_INTERVAL - 10 * SEC);
        } else {
            int byTimeInterval = Integer.valueOf(GlobalVariables.FREQUENCIES[GlobalVariables.getFrequencyIndex(this)]);
            byTimeInterval = HOUR * byTimeInterval;

            locationRequest.setInterval(byTimeInterval);
            locationRequest.setFastestInterval(byTimeInterval - MIN * 10);
        }
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if(GlobalVariables.isUpdateOnMove(this))
            locationRequest.setSmallestDisplacement(LOCATION_MIN_DISTANCE);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback, null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if(fusedLocationClient != null)
                fusedLocationClient.removeLocationUpdates(locationCallback);
        } catch (Exception ex) {

        }
//        if (mLocationManager != null) {
//            try {
//                fusedLocationClient.removeLocationUpdates(locationCallback);
//
//               // mLocationManager.removeUpdates(timeLocationListener);
//               // mLocationManager.removeUpdates(realTimeLocationListener);
//            } catch (Exception ex) {
//
//            }
//        }
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void sendLocationUpdates(final Location location) {
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

            SendLocationRequest jsonObjectRequest = new SendLocationRequest(param,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            updateLocationInfo(location, true);

                            sendBroadcastMessage();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    updateLocationInfo(location, false);

                    sendBroadcastMessage();
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

    private void sendBroadcastMessage() {
        Intent intent = new Intent(ACTION_LOCATION_UPDATED);

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private synchronized void updateLocationInfo(Location location, boolean success) {
        realm.beginTransaction();
        final LocationInfo locationInfo = realm.createObject(LocationInfo.class);
        locationInfo.setLatitude(location.getLatitude());
        locationInfo.setLongitude(location.getLongitude());
        locationInfo.setManually(false);
        locationInfo.setOnMove(GlobalVariables.isUpdateOnMove(this));
        locationInfo.setDate(Calendar.getInstance().getTimeInMillis());
        locationInfo.setSuccess(success);

        realm.commitTransaction();
    }
}