package com.gocodes.locationtracker.db;

import com.gocodes.locationtracker.model.LocationInfo;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by vit-vetal- on 05.11.17.
 */

public class LocalDB {
    private Realm realm;

    public LocalDB() {
        realm = Realm.getDefaultInstance();
    }

    public void addLocationInfo(LocationInfo locationInfo) {
        realm.beginTransaction();
        realm.copyToRealm(locationInfo);
        realm.commitTransaction();
    }

    public RealmResults<LocationInfo> getAllLocationInfo() {
        return realm.where(LocationInfo.class).findAll();
    }
}
