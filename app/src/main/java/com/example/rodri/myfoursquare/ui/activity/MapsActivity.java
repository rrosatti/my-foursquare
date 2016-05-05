package com.example.rodri.myfoursquare.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.example.rodri.myfoursquare.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by rodri on 5/5/2016.
 */
public class MapsActivity extends Activity {

    private LatLng pos = new LatLng(41.0082376, 28.97835889999999);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps_activity);

        try {
            if (map == null) {
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            }
            map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Marker marker = map.addMarker(new MarkerOptions().position(pos).title("First marker"));

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));

        } catch (Exception e) {
            e.printStackTrace();
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println("latitude: " + latLng.latitude + " longitude: " + latLng.longitude);
            }
        });


    }
}
