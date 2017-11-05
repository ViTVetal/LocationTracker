package com.gocodes.locationtracker.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by vit-vetal- on 05.11.17.
 */

public class LocationInfo extends RealmObject {


    private long date;

    private double longitude;

    private double latitude;

    private boolean success;

    private boolean onMove;

    private boolean byTime;

    private boolean realTime;

    private boolean manually;


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isOnMove() {
        return onMove;
    }

    public void setOnMove(boolean onMove) {
        this.onMove = onMove;
    }

    public boolean isByTime() {
        return byTime;
    }

    public void setByTime(boolean byTime) {
        this.byTime = byTime;
    }

    public boolean isRealTime() {
        return realTime;
    }

    public void setRealTime(boolean realTime) {
        this.realTime = realTime;
    }

    public boolean isManually() {
        return manually;
    }

    public void setManually(boolean manually) {
        this.manually = manually;
    }
}