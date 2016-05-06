package com.example.rodri.myfoursquare.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.example.rodri.myfoursquare.ui.adapter._VenueAdapter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Venue> venues;
    ListView listVenues;
    RecyclerView venuesRecyclerView;
    _VenueAdapter _venueAdapter;
    VenueAdapter venueAdapter;

    EditText etKeyWord;
    LayoutInflater inflater = null;
    View v;

    double latitude = 0, longitude = 0;
    String keyword;

    private int PLACE_PICKER_REQUEST = 1;

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
        //listVenues = (ListView) findViewById(R.id.listVenues);
        venuesRecyclerView = (RecyclerView) findViewById(R.id.venuesRecyclerView);
        //venuesRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        venuesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        inflater = getLayoutInflater();
    }

    private void initializeCustomComponents() {
        v = inflater.inflate(R.layout.custom_keyword_dialog, null);

        etKeyWord = (EditText) v.findViewById(R.id.etKeyword);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(this, data);
            LatLng latLng = place.getLatLng();
            latitude = latLng.latitude;
            longitude = latLng.longitude;

            Toast.makeText(this, "latitude: " + latitude + " longitude: " + longitude, Toast.LENGTH_LONG).show();
            showInputDialog();
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    public void showInputDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        alertDialog.setTitle("Set a category to search");

        if (v.getParent() == null) {
            alertDialog.setView(v);
        } else {
            v = null;
            v = inflater.inflate(R.layout.custom_keyword_dialog, null);
            initializeCustomComponents();
            alertDialog.setView(v);
        }

        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                keyword = etKeyWord.getText().toString();

                if (keyword.equals("")) {
                    Toast.makeText(MainActivity.this, "You need to type something!", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    new AsyncTaskParseJSON().execute();
                }
            }
        });

        alertDialog.show();
    }

    /**public void showInputDialog() {
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


    } */

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
                JSONObject json;
                if (latitude != 0 && longitude != 0) {
                     json = remoteFetch.getJSON(MainActivity.this, latitude, longitude, keyword);
                } else {
                    json = remoteFetch.getJSON(MainActivity.this, 40.7828647, -73.96535510000001, "coffee");
                }

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

            //_venueAdapter = new _VenueAdapter(MainActivity.this, 0, venueList);
            venueAdapter = new VenueAdapter(venueList);
            //listVenues.setAdapter(_venueAdapter);

            venuesRecyclerView.setAdapter(venueAdapter);
        }
    }

}
