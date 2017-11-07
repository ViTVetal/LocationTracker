package com.gocodes.locationtracker.ui.activities;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.model.LocationInfo;
import com.gocodes.locationtracker.ui.adapters.LocationInfoAdapter;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class HistoryActivity extends AppCompatActivity {
    private RecyclerView rvHistory;
    private SwipeRefreshLayout swipeRefreshLayout;

    private Realm realm;

    private RealmResults<LocationInfo> locationInfos;

    private LocationInfoAdapter locationInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar tb = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.history));

        rvHistory = (RecyclerView) findViewById(R.id.rvHistory);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        locationInfos = realm.where(LocationInfo.class).findAll().sort("date", Sort.DESCENDING);

                        locationInfoAdapter.notifyDataSetChanged();

                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        );

        realm = Realm.getDefaultInstance();

        locationInfos = realm.where(LocationInfo.class).findAll().sort("date", Sort.DESCENDING);

        locationInfoAdapter = new LocationInfoAdapter(locationInfos, this);

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        rvHistory.setLayoutManager(layoutManager);
        rvHistory.setAdapter(locationInfoAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
