package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent loginIntent = new Intent(this, loginActivity.class);
        startActivity(loginIntent);
        //Intent loginIntent = new Intent(this, loginActivity.class);
        //startActivity(loginIntent);

        //Map open intent
        //Intent mapsIntent = new Intent(this, MapsActivity.class);
        //startActivity(mapsIntent);
    }
}