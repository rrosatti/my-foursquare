package com.example.rodri.myfoursquare.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rodri.myfoursquare.R;
import com.example.rodri.myfoursquare.location.Venue;

import java.util.ArrayList;

/**
 * Created by rodri on 5/4/2016.
 */
public class VenueAdapter extends ArrayAdapter<Venue> {

    private Activity activity;
    private LayoutInflater inflater = null;
    private ArrayList<Venue> venues;

    public VenueAdapter(Activity activity, int textViewSourceId, ArrayList<Venue> venues) {
        super(activity, textViewSourceId, venues);
        try {
            this.activity = activity;
            this.venues = venues;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return venues.size();
    }

    public class ViewHolder {
        public TextView displayVenueName;
        public TextView displayVenueAddress;
        public TextView displayVenueLocation;
        public TextView displayVenueCategory;
        public TextView displayVenueHereNow;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder = new ViewHolder();
        if (convertView == null) {
            v = inflater.inflate(R.layout.custom_venue_item, null);

            holder.displayVenueName = (TextView) v.findViewById(R.id.txtVenueName);
            holder.displayVenueAddress = (TextView) v.findViewById(R.id.txtVenueAddress);
            holder.displayVenueLocation = (TextView) v.findViewById(R.id.txtVenueLocation);
            holder.displayVenueCategory = (TextView) v.findViewById(R.id.txtVenueCategory);
            holder.displayVenueHereNow = (TextView) v.findViewById(R.id.txtVenueHereNow);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.displayVenueName.setText(venues.get(position).getName());
        holder.displayVenueAddress.setText(venues.get(position).getAddress());
        holder.displayVenueLocation.setText(venues.get(position).getCity() + ", " +
                venues.get(position).getState() + ", " +
                venues.get(position).getCountry());
        holder.displayVenueCategory.setText(venues.get(position).getCategoryName());
        holder.displayVenueHereNow.setText(venues.get(position).getHereNow());

        return v;
    }

}
