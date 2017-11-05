package com.gocodes.locationtracker;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.gocodes.locationtracker.network.API;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import io.realm.Realm;

/**
 * Created by vit-vetal- on 29.10.17.
 */

public class TrackerApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Iconify.with(new FontAwesomeModule());

        RequestQueue queue = API.getInstance(this.getApplicationContext()).getRequestQueue();

        Realm.init(this);
    }
}