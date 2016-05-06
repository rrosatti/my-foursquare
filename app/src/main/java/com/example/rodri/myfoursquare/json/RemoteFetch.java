package com.example.rodri.myfoursquare.json;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by rodri on 5/3/2016.
 */
public class RemoteFetch {

    private static final String CLIENT_ID = "4MHKT0WWVGFWSXJZL5TBM4DRNKXW1SVLHXPR1OFN4ARDVKZ0";
    private static final String CLIENT_SECRET = "2JXYIWMFGLA0VTPMD3I4K01KIWDMHXJCJGZL0FFAHRAJILUT";
    private static final String VERSION = "20130815";
    private static final String LIMIT = "10";
    private static final String OPEN_SEARCH_FOURSQUARE_API = "https://api.foursquare.com/v2/venues/search?client_id="
            + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=" + VERSION + "&limit=" + LIMIT;

    public JSONObject getJSON(Context context, double lat, double lon, String query) {
        String stringURL = OPEN_SEARCH_FOURSQUARE_API;
        stringURL += "&ll=" + lat + "," + lon + "&query=" + query;

        try {
            URL url = new URL(stringURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp = "";
            while((tmp = reader.readLine()) != null) {
                json.append(tmp).append("\n");
            }
            reader.close();

            //System.out.println(json.toString());

            JSONObject data = new JSONObject(json.toString());

            if (data.getJSONObject("meta").getInt("code") != 200) {
                return null;
            }

            return data;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
