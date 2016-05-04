package com.example.rodri.myfoursquare.ui.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Venue> venues;
    ListView listVenues;
    RemoteFetch remoteFetch;

    EditText etLat;
    EditText etLon;
    EditText etKeyWord;
    LayoutInflater inflater = null;
    View v;

    double latitude, longitude;
    String keyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initializeCustomComponents();

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
            showInputDialog();
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
            }
        });

        alertDialog.show();


    }

    public class AsyncTaskParseJSON extends AsyncTask<String, Integer, ArrayList<Venue>> {

        JSONArray venueJsonArray;
        ArrayList<Venue> venueList;

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected ArrayList<Venue> doInBackground(String... params) {
            try {

                RemoteFetch remoteFetch = new RemoteFetch();
                JSONObject json = remoteFetch.getJSON(MainActivity.this, latitude, longitude, keyword);

                venueJsonArray = json.getJSONObject("response").getJSONArray("venues");

                for (int i = 0; i < venueJsonArray.length(); i++) {
                    JSONObject object = venueJsonArray.getJSONObject(i);
                    Venue venue = new Venue();

                    venue.setName(object.getString("name"));
                    venue.setAddress(object.getJSONObject("location").getString("address") + ","
                            + object.getJSONObject("location").getString("crossStreet"));
                    venue.setCategoryName(object.getJSONObject("categories").getString("shortName"));
                    venue.setCity(object.getJSONObject("location").getString("city"));
                    venue.setState(object.getJSONObject("location").getString("state"));
                    venue.setCountry(object.getJSONObject("location").getString("country"));
                    venue.setHereNow(object.getJSONObject("hereNow").getInt("count"));

                    venueList.add(venue);
                }

            } catch (Exception e) {

            }

            return venueList;
        }

        @Override
        protected void onPostExecute(ArrayList<Venue> result) {
            super.onPostExecute(result);

            // Set the custom Adapter right here!!!
        }
    }

}
