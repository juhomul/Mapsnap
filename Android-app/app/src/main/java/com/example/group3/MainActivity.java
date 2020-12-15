package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(SaveSharedPreference.getStayLogged(MainActivity.this).length() == 0)
        {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
        else
        {
            Intent mapsIntent = new Intent(this, MapsActivity.class);
            startActivity(mapsIntent);
            finish();
        }

        //Version 1.0
    }
}
