package com.gocodes.locationtracker.ui.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.model.LocationInfo;
import com.gocodes.locationtracker.ui.activities.HistoryActivity;
import com.gocodes.locationtracker.ui.activities.MapActivity;
import com.gocodes.locationtracker.utils.SizeConverter;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.Calendar;
import java.util.List;

import io.realm.RealmResults;

/**
 * Created by vit-vetal- on 06.11.17.
 */

public class LocationInfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity activity;
    private RealmResults<LocationInfo> mDataset;

    public static class LocationInfoItem extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView tvDate;
        public TextView tvLocation;
        public ImageView ivClock;
        public ImageView ivLocation;
        public ImageView ivSuccess;
        public ImageView ivMove;
        public LocationInfoItem(View v) {
            super(v);
        }
    }

    public LocationInfoAdapter(RealmResults<LocationInfo> mDataset, Activity activity) {
        this.mDataset = mDataset;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_info_item, parent, false);

        LocationInfoItem vh = new LocationInfoItem(v);

        vh.tvDate = (TextView) v.findViewById(R.id.tvDate);
        vh.tvLocation = (TextView) v.findViewById(R.id.tvLocation);

        vh.ivClock = (ImageView) v.findViewById(R.id.ivClock);
        vh.ivClock.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_clock_o)
                .color(Color.GRAY));
        vh.ivLocation = (ImageView) v.findViewById(R.id.ivLocation);
        vh.ivLocation.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_map_marker)
                .color(Color.GRAY));

        vh.ivSuccess = (ImageView) v.findViewById(R.id.ivSuccess);
        vh.ivMove = (ImageView) v.findViewById(R.id.ivMove);

        vh.cardView = (CardView) v.findViewById(R.id.cardView);

        return vh;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final LocationInfo locationInfo = mDataset.get(position);

        LocationInfoItem locationInfoItem = (LocationInfoItem) holder;

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(locationInfo.getDate());
        final String date = DateFormat.format("MM-dd-yyyy HH:mm:ss", cal).toString();
        locationInfoItem.tvDate.setText(date);

        locationInfoItem.tvLocation.setText(locationInfo.getLatitude() + " " + locationInfo.getLongitude());

        if(locationInfo.isSuccess()) {
            locationInfoItem.ivSuccess.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_check)
                    .color(Color.GREEN));
        } else {
            locationInfoItem.ivSuccess.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_times)
                    .color(Color.RED));
        }


        if(locationInfo.isOnMove()) {
            locationInfoItem.ivMove.setImageDrawable(new IconDrawable(activity, FontAwesomeIcons.fa_arrows)
                    .color(Color.GRAY));
        } else {
            locationInfoItem.ivMove.setImageDrawable(null);
        }

        locationInfoItem.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MapActivity.class);
                intent.putExtra("latitude", locationInfo.getLatitude());
                intent.putExtra("longitude", locationInfo.getLongitude());
                intent.putExtra("date", date);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}