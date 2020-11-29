package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    Button buttonTurha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SaveSharedPreference.clearUser(this);


        //jos löytyy tallennettu user nii menee suoraa loginnin ohi
        if(SaveSharedPreference.getStayLogged(MainActivity.this).length() == 0)
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            startActivity(mapsIntent);
            finish();
        }

        buttonTurha = findViewById(R.id.buttonTurha);
        buttonTurha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });
        //Map open intent
        //Intent mapsIntent = new Intent(this, MapsActivity.class);
        //startActivity(mapsIntent);

        //ProfileActivity
        //Intent profileIntent = new Intent(this, ProfileActivity.class);
        //startActivity(profileIntent);

        //Camera
        //Intent cameraIntent = new Intent(this, cameraActivity.class);
        //startActivity(cameraIntent);

        //SaveStory
        //Intent createStoryIntent = new Intent(this, CreateStoryActivity.class);
        //startActivity(createStoryIntent);

        //Version 0.4
    }

    /*
    @Override
    protected void onResume() {
        super.onResume();

        if (SaveSharedPreference.getToken(MainActivity.this).length() == 0) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {

            Intent mapsIntent = new Intent(this, MapsActivity.class);
            startActivity(mapsIntent);
            //finish();
        }

    }
     */
}