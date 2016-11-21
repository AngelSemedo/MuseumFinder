package es.unex.saee.museumfinder;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListPlacesActivity extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new HttpGetTask().execute();
    }

    @SuppressWarnings("deprecation")
    private class HttpGetTask extends AsyncTask<Void, Void, List<String>> {

        private static final String API_KEY = "AIzaSyCIO_z2BMgUA604K5it93gqknDuonSktnI";
        SharedPreferences prefs = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        private Bundle bundle = getIntent().getExtras();
        private String latitude = "39.472269";
        private String longitude = "-6.378295";
        private String radius = prefs.getString("radius", "5000");
        private final String TYPE = bundle.getString("type");

        private final String URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=" + latitude + "," + longitude +
                "&radius=" + radius +
                "&types=" + TYPE +
                "&key=" + API_KEY;

        AndroidHttpClient mClient = AndroidHttpClient.newInstance("");

        @Override
        protected List<String> doInBackground(Void... params) {
            Log.d("Background", URL);
            HttpGet request = new HttpGet(URL);
            JSONResponseHandler responseHandler = new JSONResponseHandler();
            try {
                return mClient.execute(request, responseHandler);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (null != mClient)
                mClient.close();
            setListAdapter(new ArrayAdapter<String>(
                    ListPlacesActivity.this,
                    R.layout.list_item, result));
        }
    }

    @SuppressWarnings("deprecation")
    private class JSONResponseHandler implements ResponseHandler<List<String>> {

        private static final String RESULTS_TAG = "results";
        private static final String NAME_TAG = "name";
        private static final String VICINITY_TAG = "vicinity";
        private static final String PLACEID_TAG = "place_id";

        private static final String GEO_TAG = "geometry";
        private static final String LOC_TAG = "location";
        private static final String LAT_TAG = "lat";
        private static final String LONG_TAG = "lng";


        @Override
        public List<String> handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
            List<String> result = new ArrayList<String>();
            String JSONResponse = new BasicResponseHandler().handleResponse(response);
            try {
                JSONObject responseObject = (JSONObject) new JSONTokener(JSONResponse).nextValue();
                JSONArray places = responseObject.getJSONArray(RESULTS_TAG);
                for (int idx = 0; idx < places.length(); idx++) {

                    JSONObject gplace = (JSONObject) places.get(idx);
                    JSONObject geo = (JSONObject) gplace.get(GEO_TAG);
                    JSONObject latlong = (JSONObject) geo.get(LOC_TAG);
                    result.add(gplace.get(NAME_TAG) + "\n"
                            + gplace.getString(VICINITY_TAG) + "\n"
                            + latlong.get(LAT_TAG) + ", " + latlong.get(LONG_TAG));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
