package com.example.rodri.myfoursquare.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.rodri.myfoursquare.R;
import com.example.rodri.myfoursquare.json.RemoteFetch;
import com.example.rodri.myfoursquare.location.Venue;
import com.example.rodri.myfoursquare.ui.adapter.VenueAdapter;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Venue> venues;
    ListView listVenues;
    RemoteFetch remoteFetch;
    VenueAdapter venueAdapter;

    EditText etLat;
    EditText etLon;
    EditText etKeyWord;
    LayoutInflater inflater = null;
    View v;

    double latitude, longitude;
    String keyword;

    private int PLACE_PICKER_REQUEST = 1;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(54.69726685890506,-2.7379201682812226), new LatLng(55.38942944437183, -1.2456105979687226));



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initializeCustomComponents();

        new AsyncTaskParseJSON().execute();



    }

    private void initialize() {
        venues = new ArrayList<>();
        listVenues = (ListView) findViewById(R.id.listVenues);
        inflater = getLayoutInflater();
    }

    private void initializeCustomComponents() {
        v = inflater.inflate(R.layout.custom_search_venue_dialog, null);

        etLat = (EditText) v.findViewById(R.id.etLat);
        etLon = (EditText) v.findViewById(R.id.etLon);
        etKeyWord = (EditText) v.findViewById(R.id.etKeyWord);
    }

    private ArrayList<Venue> getVenues() {
        ArrayList<Venue> _venues = new ArrayList<>();
        return _venues;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        RelativeLayout relativeLayout = (RelativeLayout) menu.findItem(R.id.menuSearch).getActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuSearch) {
            //showInputDialog();

            /*Intent showMaps = new Intent(MainActivity.this, MapsActivity.class);
            startActivity(showMaps);*/

            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                Intent intent = builder.build(this);
                startActivityForResult(intent, PLACE_PICKER_REQUEST);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void showInputDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Search for a location");

        if (v.getParent() == null) {
            alertDialog.setView(v);
        } else {
            v = null;
            initializeCustomComponents();
            alertDialog.setView(v);
        }


        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                System.out.println("lat " + etLat.getText().toString() + " lon " + etLon.getText().toString()
                        + " keyword " + etKeyWord.getText().toString());

                if (etLat.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.latitude_empty, Toast.LENGTH_LONG).show();
                    return;
                } else if (etLon.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.longitude_empty, Toast.LENGTH_LONG).show();
                    return;
                } else if (etKeyWord.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, R.string.keyword_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                latitude = Double.parseDouble(etLat.getText().toString());
                longitude = Double.parseDouble(etLon.getText().toString());
                keyword = etKeyWord.getText().toString();

                new AsyncTaskParseJSON().execute();
            }
        });

        alertDialog.show();


    }

    public class AsyncTaskParseJSON extends AsyncTask<String, Integer, ArrayList<Venue>> {

        JSONArray venueJsonArray;
        ArrayList<Venue> venueList = new ArrayList<>();

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Venue> doInBackground(String... params) {
            try {

                RemoteFetch remoteFetch = new RemoteFetch();
                JSONObject json = remoteFetch.getJSON(MainActivity.this, 40.7, -74, "coffee");


                venueJsonArray = json.getJSONObject("response").getJSONArray("venues");

                for (int i = 0; i < venueJsonArray.length(); i++) {
                    System.out.println("I've been here!");
                    JSONObject object = venueJsonArray.getJSONObject(i);
                    Venue venue = new Venue();

                    venue.setName(object.getString("name"));
                    venue.setAddress(object.getJSONObject("location").optString("address"));
                    venue.setCategoryName(object.getJSONArray("categories").getJSONObject(0).optString("shortName"));
                    venue.setCity(object.getJSONObject("location").optString("city"));
                    venue.setState(object.getJSONObject("location").optString("state"));
                    venue.setCountry(object.getJSONObject("location").optString("country"));
                    venue.setHereNow(object.getJSONObject("hereNow").optInt("count"));

                    venueList.add(venue);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return venueList;
        }

        @Override
        protected void onPostExecute(ArrayList<Venue> result) {
            super.onPostExecute(result);

            venueAdapter = new VenueAdapter(MainActivity.this, 0, venueList);
            listVenues.setAdapter(venueAdapter);
        }
    }

}
