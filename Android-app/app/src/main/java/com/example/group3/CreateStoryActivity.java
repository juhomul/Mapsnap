package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class CreateStoryActivity extends AppCompatActivity {

    Button btnSaveStory;
    TextView textLocation;
    FusedLocationProviderClient client;
    String userLocationAPI;
    ImageView displayImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        Intent intent = getIntent();
        Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");
        client = LocationServices.getFusedLocationProviderClient(this);
        textLocation = findViewById(R.id.textViewLocation);

        displayImageView = findViewById(R.id.imageDisplayView);
        displayImageView.setImageBitmap(image);

        btnSaveStory = findViewById(R.id.buttonSaveStory);
        btnSaveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SAVE IMAGE //
            }
        });

        //Display Latitude and Longitude when this activity opens
        //so when picture has been taken and moved into this window
        getLocation();

    }

    private void getLocation() {
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
                    String userLocation = String.valueOf(userLat);
                    userLocation = "Latitude: " +  userLocation + "\n" + "Longitude: " + String.valueOf(userLong);
                    textLocation.setText(userLocation);
                    userLocationAPI = userLocation;
                }
            }
        });
    }


}