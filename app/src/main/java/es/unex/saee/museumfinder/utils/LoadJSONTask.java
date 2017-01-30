package es.unex.saee.museumfinder.utils;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import es.unex.saee.museumfinder.model.GooglePlace;
import es.unex.saee.museumfinder.model.Response;

/**
 * Created by Usuario on 30/01/2017.
 */

public class LoadJSONTask extends AsyncTask<String, Void, Response>{

    public interface Listener{
        void onLoaded(List<GooglePlace> placesList);
        void onError();
    }

    private Listener mListener;

    public LoadJSONTask(Listener listener){
        mListener = listener;
    }

    public String loadJSON(String jsonURL) throws IOException{
        URL url = new URL(jsonURL);
        Log.i("LOAD JSON", url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();

        while((line = in.readLine()) != null){
            response.append(line);
        }

        in.close();
        return response.toString();
    }

    @Override
    protected Response doInBackground(String... strings){
        try{
            Response response = new Response();
            String stringResponse = loadJSON(strings[0]);
            Log.i("JSON RECEIVED", stringResponse);

            JSONObject result = new JSONObject(stringResponse);
            JSONArray allPlaces = result.getJSONArray("results");
            for(int i = 0; i < allPlaces.length(); i++){
                GooglePlace gplace = new GooglePlace();
                JSONObject place_info = (JSONObject) allPlaces.get(i);
                JSONObject place_location = (JSONObject) place_info.get("geometry");
                JSONObject place_latlng = (JSONObject) place_location.get("location");

                gplace.setName(place_info.getString("name"));
                Log.i("NAME", gplace.getName());
                gplace.setAddress(place_info.getString("vicinity"));
                gplace.setPlace_id(place_info.getString("place_id"));
                gplace.setLatitude(place_latlng.getString("lat"));
                gplace.setLongitude(place_latlng.getString("lng"));

                response.addPlace(gplace);
            }
            return response;
        } catch (IOException e){
            e.printStackTrace();
            return null;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Response response){
        if(response != null){
            mListener.onLoaded(response.getPlaces());
        } else {
            mListener.onError();
        }
    }

}
