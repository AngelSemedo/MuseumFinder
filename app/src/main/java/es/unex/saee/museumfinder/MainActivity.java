package es.unex.saee.museumfinder;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        final Button museumButton = (Button) findViewById(R.id.btn_museum);
        museumButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "museum");

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ListPlacesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );

        final Button galleryButton = (Button) findViewById(R.id.btn_gallery);
        galleryButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "art_gallery");

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ListPlacesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );

        final Button powButton = (Button) findViewById(R.id.btn_worship);
        powButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "place_of_worship");

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ListPlacesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );

        final Button allButton = (Button) findViewById(R.id.btn_all);
        allButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Bundle bundle = new Bundle();
                        bundle.putString("type", "museum%7Cart_gallery%7Cplace_of_worship");

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, ListPlacesActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
        );

        final Button favButton = (Button) findViewById(R.id.btn_favs);
        favButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, FavsActivity.class);
                        startActivity(intent);
                    }
                }
        );

        final Button loginButton = (Button) findViewById(R.id.btn_login);
        loginButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, FavsActivity.class);
                        startActivity(intent);
                    }
                }
        );


        final Button settingsButton = (Button) findViewById(R.id.btn_settings);
        settingsButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}
