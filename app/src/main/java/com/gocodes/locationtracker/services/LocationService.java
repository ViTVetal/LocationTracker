package com.gocodes.locationtracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.gocodes.locationtracker.utils.GlobalVariables;
import com.gocodes.locationtracker.utils.LogWriter;

public class LocationService extends Service {
    private static final String TAG = "tracker_service";
    private LocationManager mLocationManager = null;

    private static final float LOCATION_MIN_DISTANCE = 10f;
    private static final int REAL_TIME_INTERVAL = 60000;

    private class TimeLocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public TimeLocationListener(String provider) {
            LogWriter.writeToFile( "Time " + provider );
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            LogWriter.writeToFile( "Time: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogWriter.writeToFile( "Time: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogWriter.writeToFile( "Time: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogWriter.writeToFile( "Time: " + provider);
        }
    }

    private class MovementLocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public MovementLocationListener(String provider) {
            LogWriter.writeToFile( "Movement " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            LogWriter.writeToFile( "Movement: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogWriter.writeToFile( "Movement: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogWriter.writeToFile( "Movement: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogWriter.writeToFile( "Movement: " + provider);
        }
    }

    private class RelaTimeLocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public RelaTimeLocationListener(String provider) {
            LogWriter.writeToFile( "RealTime: " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            LogWriter.writeToFile( "RealTime: " + location);
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {
            LogWriter.writeToFile( "RealTime: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            LogWriter.writeToFile( "RealTime: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LogWriter.writeToFile( "RealTime: " + provider);
        }
    }

    TimeLocationListener[] byTimeLocationListeners = new TimeLocationListener[] {
            new TimeLocationListener(LocationManager.GPS_PROVIDER),
            new TimeLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    MovementLocationListener[] byMovementLocationListeners = new MovementLocationListener[] {
            new MovementLocationListener(LocationManager.GPS_PROVIDER),
            new MovementLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    RelaTimeLocationListener[] relaTimetLocationListeners = new RelaTimeLocationListener[] {
            new RelaTimeLocationListener(LocationManager.GPS_PROVIDER),
            new RelaTimeLocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogWriter.writeToFile( "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        LogWriter.writeToFile( "onCreate");
        initializeLocationManager();

        int byTimeInterval = Integer.valueOf(GlobalVariables.FREQUENCIES[GlobalVariables.getFrequencyIndex(this)]);
        byTimeInterval *= 1000;

        if(GlobalVariables.isRealTimeUpdate(this)) {
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, REAL_TIME_INTERVAL, 0,
                        relaTimetLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, REAL_TIME_INTERVAL, 0,
                        relaTimetLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        } else {
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, byTimeInterval, 0,
                        byTimeLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, byTimeInterval, 0,
                        byTimeLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile("fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

        boolean updateOnMove = GlobalVariables.isUpdateOnMove(this);

        if(updateOnMove) {
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER, 0, LOCATION_MIN_DISTANCE,
                        byMovementLocationListeners[1]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile( "fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "network provider does not exist, " + ex.getMessage());
            }
            try {
                mLocationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER, 0, LOCATION_MIN_DISTANCE,
                        byMovementLocationListeners[0]);
            } catch (java.lang.SecurityException ex) {
                LogWriter.writeToFile( "fail to request location update, ignore" + ex);
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "gps provider does not exist " + ex.getMessage());
            }
        }

    }

    @Override
    public void onDestroy() {
        LogWriter.writeToFile( "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < byTimeLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(byTimeLocationListeners[i]);
                    mLocationManager.removeUpdates(byMovementLocationListeners[i]);
                    mLocationManager.removeUpdates(relaTimetLocationListeners[i]);
                } catch (Exception ex) {
                    LogWriter.writeToFile( "fail to remove location listners, ignore" + ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        LogWriter.writeToFile( "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}