package es.unex.saee.museumfinder.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.unex.saee.museumfinder.R;
import es.unex.saee.museumfinder.model.GooglePlace;
import es.unex.saee.museumfinder.utils.LoadJSONTask;

public class ListPlacesActivity extends AppCompatActivity implements LoadJSONTask.Listener, AdapterView.OnItemClickListener{

    private ListView mListView;
    private List<HashMap<String, String>> mPlacesList = new ArrayList<>();
    //private Bundle bundle = getIntent().getExtras();

    private static String URL;
    private static String API_KEY = "AIzaSyCIO_z2BMgUA604K5it93gqknDuonSktnI";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDR = "vicinity";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";
    private static final String KEY_PREF_RADIUS = "pref_radius";
    private static String pref_radius;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_places);

        Geocoder geocoder;
        String bestProvider, type;
        List<Address> user = null;
        double lat, lng;
        Intent intent = getIntent();
        if (intent == null){
            Log.i("INTENT", "Null intent");
        } else {
            Log.i("INTENT", "Not null intent ->" + intent.toString());
        }
        type = getIntent().getStringExtra("type");

        if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);
        }

        LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        bestProvider = lm.getBestProvider(criteria, true);
        Location location = lm.getLastKnownLocation(bestProvider); // Permission in manifest

        if(location == null){
            Log.i("LOCATION", "Unable to find location");
        } else {
            geocoder = new Geocoder(this);
            try{
                user = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                lat = user.get(0).getLatitude();
                lng = user.get(0).getLongitude();
                //type = bundle.getString("type");
                PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                pref_radius = preferences.getString(KEY_PREF_RADIUS, "1000");
                Log.i("RADIUS", pref_radius);

                Log.i("LOCATION", Double.toString(lat) + ", " + Double.toString(lng));
                URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                        lat + "," + lng +
                        "&radius=" + pref_radius +
                        "&types=" + type +
                        "&key=" + API_KEY;
                Log.i("URL", URL);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setOnItemClickListener(this);
        new LoadJSONTask(this).execute(URL);
    }

    private void loadListView(){
        ListAdapter adapter = new SimpleAdapter(ListPlacesActivity.this,
                mPlacesList,
                R.layout.list_item,
                new String[] {KEY_NAME, KEY_ADDR},
                new int[] {R.id.name, R.id.vicinity});

        mListView.setAdapter(adapter);
    }

    @Override
    public void onLoaded(List<GooglePlace> placesList){
        Log.i("LOADED PLACES", Integer.toString(placesList.size()));
        if(placesList.size() == 0){
            Toast.makeText(this, "No nearby places found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, placesList.size() + " places found", Toast.LENGTH_SHORT).show();
        }
        for(int i = 0; i < placesList.size(); i++){
            GooglePlace gplace = placesList.get(i);
            Log.i("ADDED", gplace.getName());
            HashMap<String, String> map = new HashMap<>();
            map.put(KEY_NAME, gplace.getName());
            map.put(KEY_ADDR, gplace.getAddress());
            map.put(KEY_LAT, gplace.getLatitude());
            map.put(KEY_LNG, gplace.getLongitude());

            mPlacesList.add(map);
        }
        loadListView();
    }

    @Override
    public void onError(){
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
        Log.i("CLICKED", mPlacesList.get(i).get(KEY_NAME));

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("detail_name", mPlacesList.get(i).get(KEY_NAME));
        intent.putExtra("detail_addr", mPlacesList.get(i).get(KEY_ADDR));
        intent.putExtra("detail_lat", mPlacesList.get(i).get(KEY_LAT));
        intent.putExtra("detail_lng", mPlacesList.get(i).get(KEY_LNG));

        startActivity(intent);
    }

}
