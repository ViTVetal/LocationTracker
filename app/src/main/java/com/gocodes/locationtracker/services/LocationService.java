package com.gocodes.locationtracker.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.network.requests.SendLocationRequest;
import com.gocodes.locationtracker.ui.activities.MainActivity;
import com.gocodes.locationtracker.utils.GlobalVariables;
import com.gocodes.locationtracker.utils.LogWriter;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LocationService extends Service {
    private static final String TAG = "tracker_service";
    private LocationManager mLocationManager = null;

    private static final float LOCATION_MIN_DISTANCE = 10f;
    private static final int REAL_TIME_INTERVAL = 60000;

    private class TimeLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("myLogs", location + "");
            LogWriter.writeToFile( "Time: " + location);

            sendLocationUpdates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("myLogs","onProviderDisabled Time: " + provider);
            LogWriter.writeToFile( "Time: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("myLogs","onProviderEnabled Time: " + provider);
            LogWriter.writeToFile( "Time: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("myLogs","onStatusChanged Time: " + provider);
            LogWriter.writeToFile( "Time: " + provider);
        }
    }

    private class MovementLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("myLogs", location + "");
            LogWriter.writeToFile( "Movement: " + location);

            sendLocationUpdates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("myLogs","onProviderDisabled Movement: " + provider);
            LogWriter.writeToFile( "Movement: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("myLogs","onProviderEnabled Movement: " + provider);
            LogWriter.writeToFile( "Movement: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("myLogs","onStatusChanged Movement: " + provider);
            LogWriter.writeToFile( "Movement: " + provider);
        }
    }

    private class RealTimeLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("myLogs", location + "");
            LogWriter.writeToFile( "RealTime: " + location);

            sendLocationUpdates(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("myLogs","onProviderDisabled RealTime: " + provider);
            LogWriter.writeToFile( "RealTime: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("myLogs","onProviderEnabled RealTime: " + provider);
            LogWriter.writeToFile( "RealTime: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("myLogs","onStatusChanged RealTime: " + provider);
            LogWriter.writeToFile( "RealTime: " + provider);
        }
    }

    TimeLocationListener timeLocationListener = new TimeLocationListener();

    MovementLocationListener movementLocationListener = new MovementLocationListener();

    RealTimeLocationListener realTimeLocationListener = new RealTimeLocationListener();

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("myLogs","onStartCommand");
        LogWriter.writeToFile( "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d("myLogs","onCreate");
        LogWriter.writeToFile( "onCreate");
        initializeLocationManager();

        int byTimeInterval = Integer.valueOf(GlobalVariables.FREQUENCIES[GlobalVariables.getFrequencyIndex(this)]);
        byTimeInterval = 1000 * 120;

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        Log.d("myLogs", "oncreate " + mLocationManager);
        if(GlobalVariables.isRealTimeUpdate(this)) {
            try {
                mLocationManager.requestLocationUpdates(
                        REAL_TIME_INTERVAL, 0, criteria,
                        realTimeLocationListener, null);
            } catch (java.lang.SecurityException ex) {
                Log.d("myLogs","fail to request location update, ignore" + ex);
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d("myLogs","network provider does not exist, " + ex.getMessage());
                LogWriter.writeToFile( "network provider does not exist, " + ex.getMessage());
            }
        } else {
            try {
                mLocationManager.requestLocationUpdates(
                        byTimeInterval, 0, criteria,
                        timeLocationListener, null);
                Log.d("myLogs", "byTime");
            } catch (java.lang.SecurityException ex) {
                Log.d("myLogs", "fail to request location update, ignore" + ex);
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d("myLogs", "network provider does not exist, " + ex.getMessage());
                LogWriter.writeToFile("network provider does not exist, " + ex.getMessage());
            }
        }

        boolean updateOnMove = GlobalVariables.isUpdateOnMove(this);

        if(updateOnMove) {
            try {
                mLocationManager.requestLocationUpdates(
                        0, LOCATION_MIN_DISTANCE, criteria,
                        movementLocationListener, null);
                Log.d("myLogs", "move");
            } catch (java.lang.SecurityException ex) {
                Log.d("myLogs",  "fail to request location update, ignore" + ex);
                LogWriter.writeToFile( "fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d("myLogs", "network provider does not exist, " + ex.getMessage());
                LogWriter.writeToFile("network provider does not exist, " + ex.getMessage());
            }
        }

    }

    @Override
    public void onDestroy() {
        Log.d("myLogs", "onDestroy");
        LogWriter.writeToFile( "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(timeLocationListener);
                mLocationManager.removeUpdates(realTimeLocationListener);
                mLocationManager.removeUpdates(movementLocationListener);
            } catch (Exception ex) {
                Log.d("myLogs", "fail to remove location listners, ignore" + ex);
                LogWriter.writeToFile( "fail to remove location listners, ignore" + ex);
            }
        }
    }

    private void initializeLocationManager() {
        Log.d("myLogs", "initializeLocationManager");
        LogWriter.writeToFile( "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    private void sendLocationUpdates(Location location) {
        if (location != null) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("email", GlobalVariables.getEmail(this));
            params.put("password", GlobalVariables.getPassword(this));
            params.put("assetId", GlobalVariables.getAssertId(this));
            params.put("latitude", String.valueOf(location.getLatitude()));
            params.put("longitude", String.valueOf(location.getLongitude()));
            params.put("enableHistory", String.valueOf(GlobalVariables.isUpdateHistory(this)));
            params.put("customValue1", "934");

            JSONObject param = new JSONObject(params);

            SendLocationRequest.send(this, param);
        }
    }
}