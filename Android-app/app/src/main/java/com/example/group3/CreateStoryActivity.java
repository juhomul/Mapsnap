package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class CreateStoryActivity extends AppCompatActivity {

    Button btnSaveStory;
    EditText textDesc;
    FusedLocationProviderClient client;
    String strLat, strLong, strDesc;
    ImageView displayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        Intent intent = getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");
        client = LocationServices.getFusedLocationProviderClient(this);

        textDesc = findViewById(R.id.textSaveStory);

        displayImageView = findViewById(R.id.imageDisplayView);
        displayImageView.setImageBitmap(image);

        Intent mapsIntent = new Intent(this, MapsActivity.class);

        btnSaveStory = findViewById(R.id.buttonSaveStory);
        btnSaveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SAVE //
                /* Apille:
                    textDesc = "desc": "string",
                    image = "image": {},
                    strLat = "lat": "string",
                    strLong = "long": "string"
                 */

                String token = SaveSharedPreference.getToken(CreateStoryActivity.this);
                strDesc = textDesc.getText().toString();
                String username = SaveSharedPreference.getUserName(CreateStoryActivity.this);

                Log.d("CreateStory","username: " + username);
                Log.d("CreateStory", "latitude: " + strLat);
                Log.d("CreateStory", "longitude: " + strLong);
                Log.d("CreateStory", "Description: " + strDesc);

                startActivity(mapsIntent);
                finish();
            }
        });

        getLocation();
    }

    private void getLocation() {
        //Check permission and get location when picture has been taken

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(CreateStoryActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();

                    strLat = String.valueOf(userLat);
                    strLong = String.valueOf(userLong);
                }
            }
        });
    }


}