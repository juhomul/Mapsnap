package com.example.group3;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DrawerLayout drawer;
    TextView showEmail, showUsername;
    String email, username;
    Marker marker;
    static final int REQUEST_IMAGE_CAPTURE = 1; //Tarviiko Juho ? Voi poistaa jos ei
    static int ACCESS_LOCATION_CODE = 1001;

    public ArrayList<LatLng> markersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        email = SaveSharedPreference.getEmail(MapsActivity.this);
        username = SaveSharedPreference.getUserName(MapsActivity.this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.map_view);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        showEmail = headerView.findViewById(R.id.showEmail);
        showUsername = headerView.findViewById(R.id.showUsername);
        showEmail.setText(email);
        showUsername.setText(username);

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
                        SaveSharedPreference.clearUser(MapsActivity.this);
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
                switch (item.getItemId()) {
                    case R.id.menu:

                        if (!drawer.isDrawerOpen(GravityCompat.START))
                            drawer.openDrawer(GravityCompat.START);
                        else drawer.closeDrawer(GravityCompat.END);
                        return true;

                    case R.id.map_view:
                        return true;

                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(), cameraActivity.class));
                        finish(); //Tämä sulkee maps activityn joten ku kamera/CreateStory activitysta tullaan niin mennäänki suoraan mainActivityyn.
                        overridePendingTransition(0, 0);
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
    

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            enableUserLocation();
            zoomToUserLocation();
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        ACCESS_LOCATION_CODE);
            }


        }
    }

    @SuppressLint("MissingPermission")
    private void enableUserLocation() {
        mMap.setMyLocationEnabled(true);
    }

    private void zoomToUserLocation() {
        @SuppressLint("MissingPermission") Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

            }
        });
    }
    

    public ArrayList<LatLng> getMarkers(){
        //Loops and adds new markers to the list
        markersList = new ArrayList<LatLng>();

        LatLng oulu1 = new LatLng(65.0121, 25.4651);
        LatLng hukassa = new LatLng(5.0000, 15.0000);
        LatLng oulu3 = new LatLng(15.1241, 25.2121);
        LatLng oulu4 = new LatLng(64.0021, 37.1201);
        LatLng oulu5 = new LatLng(65.0822, 1.1002);

        markersList.add(oulu1);
        markersList.add(hukassa);
        markersList.add(oulu3);
        markersList.add(oulu4);
        markersList.add(oulu5);

        return markersList;

    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";
        String storyDesc = "Tässä on storyn description";

        private View view;

        public CustomInfoWindowAdapter() {
            view = getLayoutInflater().inflate(R.layout.popup,null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            if (MapsActivity.this.marker != null
                    && MapsActivity.this.marker.isInfoWindowShown()) {
                MapsActivity.this.marker.hideInfoWindow();
                MapsActivity.this.marker.showInfoWindow();
            }
            return null;
        }

        @Override
        public View getInfoWindow(final Marker marker) {
            MapsActivity.this.marker = marker;

            ImageView image = view.findViewById(R.id.image);
            TextView desc = view.findViewById(R.id.textView);

            desc.setText(storyDesc);

            Picasso.get()
                    .load(imageUri)
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(image);

            //getInfoContents(marker);
            return view;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ACCESS_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
                zoomToUserLocation();
            } else {
                //Shows dialog if permission is not granted
                Toast.makeText(MapsActivity.this,"LOCATION PERMISSION NOT GRANTED", Toast.LENGTH_LONG);
            }
        }
    }
}
