package com.gocodes.locationtracker.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.model.LocationInfo;
import com.gocodes.locationtracker.ui.adapters.LocationInfoAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);

        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);

        realm = Realm.getDefaultInstance();

        RealmResults<LocationInfo> locationInfos = realm.where(LocationInfo.class).findAll().sort("date", Sort.DESCENDING);

        LocationInfoAdapter locationInfoAdapter = new LocationInfoAdapter(locationInfos, this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(locationInfoAdapter);
    }
}
