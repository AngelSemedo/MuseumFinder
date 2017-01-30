package es.unex.saee.museumfinder.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import es.unex.saee.museumfinder.R;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Bundle bundle = getIntent().getExtras();

        this.setTitle(bundle.getString("detail_name"));

        TextView name = (TextView) findViewById(R.id.detail_name);
        name.setText(bundle.getString("detail_name"));

        TextView address = (TextView) findViewById(R.id.detail_addr);
        address.setText(bundle.getString("detail_addr"));

        String locationString = "geo:0,0?q=" +
                bundle.getString("detail_lat") + "," + bundle.getString("detail_lng") +
                "(" + bundle.getString("detail_name") + ")";
        Log.i("GEO-URI", locationString);
        final Uri geoLocation = Uri.parse(locationString);
        Button showLocation = (Button) findViewById(R.id.location_btn);
        showLocation.setOnClickListener(
                new View.OnClickListener(){
                    public void onClick(View view){
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(geoLocation);
                        if(intent.resolveActivity(getPackageManager()) != null){
                            startActivity(intent);
                        }
                    }
                }
        );
    }
}
