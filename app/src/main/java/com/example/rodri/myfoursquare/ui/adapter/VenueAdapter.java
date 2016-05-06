package com.example.rodri.myfoursquare.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rodri.myfoursquare.R;
import com.example.rodri.myfoursquare.location.Venue;

import java.util.ArrayList;

/**
 * Created by rodri on 5/6/2016.
 */
public class VenueAdapter extends RecyclerView.Adapter<VenueAdapter.MyViewHolder> {

    private ArrayList<Venue> venues;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name, address, location, category, hereNow;

        public MyViewHolder(View view) {
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            name = (TextView) view.findViewById(R.id.txtVenueName);
            address = (TextView) view.findViewById(R.id.txtVenueAddress);
            location = (TextView) view.findViewById(R.id.txtVenueLocation);
            category = (TextView) view.findViewById(R.id.txtVenueCategory);
            hereNow = (TextView) view.findViewById(R.id.txtVenueHereNow);


        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), venues.get(getPosition()).getName(), Toast.LENGTH_LONG).show();
        }



    }

    public VenueAdapter(ArrayList<Venue> venues) {
        this.venues = venues;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_venue_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Venue venue = venues.get(position);
        holder.name.setText(venue.getName());
        holder.address.setText(venue.getAddress());
        holder.location.setText(venue.getCity() + ", " + venue.getState() + ", " + venue.getCountry());
        holder.category.setText(venue.getCategoryName());
        holder.hereNow.setText(String.valueOf(venue.getHereNow()));
    }

    @Override
    public int getItemCount() {
        return venues.size();
    }
}
