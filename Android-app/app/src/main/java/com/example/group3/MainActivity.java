package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //jos löytyy tallennettu user nii menee suoraa loginnin ohi
        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
        }

        /*BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.google_map);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.google_map:
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });*/

        //Map open intent
        //Intent mapsIntent = new Intent(this, MapsActivity.class);
        //startActivity(mapsIntent);

        //ProfileActivity
        //Intent profileIntent = new Intent(this, ProfileActivity.class);
        //startActivity(profileIntent);

        //Camera
        //Intent cameraIntent = new Intent(this, cameraActivity.class);
        //startActivity(cameraIntent);
    }
}