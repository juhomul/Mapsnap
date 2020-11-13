package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class InfoActivity extends AppCompatActivity {

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.map_view);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.side_info:
                        startActivity(new Intent(getApplicationContext(), InfoActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.side_logout:
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu:
                        drawer = findViewById(R.id.drawer_layout);
                        if(!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(GravityCompat.START);
                        else drawer.closeDrawer(GravityCompat.END);
                        return true;

                    case R.id.map_view:
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
        });
    }
}