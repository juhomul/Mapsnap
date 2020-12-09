package com.example.group3;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    private DrawerLayout drawer;
    TextView showEmail, showUsername;
    String email, username;
    Marker marker;
    JSONObject markerObject;
    JSONArray markers;
    RequestQueue requestQueue;
    LatLng userLatLng;
    static int ACCESS_LOCATION_CODE = 1001;

    public ArrayList<LatLng> markersList;
    public ArrayList<Integer> markerIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

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

        requestQueue = Volley.newRequestQueue(this);

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

                    case R.id.side_delete_user:
                        new AlertDialog.Builder(MapsActivity.this)
                                .setTitle("Are you sure")
                                .setMessage("Your account and stories will be permanently deleted")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteUser deleteUser = new DeleteUser();
                                        deleteUser.deleteUserRequest("http://100.26.132.75/user/id/" + SaveSharedPreference.getUserId(MapsActivity.this), MapsActivity.this);
                                        finish();
                                        overridePendingTransition(0, 0);
                                    }
                                })

                                .setNegativeButton(android.R.string.no, null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();

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
        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        getMarkers("http://100.26.132.75/story/location");

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location enabled!", Toast.LENGTH_SHORT).show();
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

    @SuppressLint("MissingPermission")
    private void zoomToUserLocation() {
        LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnSuccessListener(
                new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10));
                    }
                }
        );


        /** //TÄMÄ VANHA EI KERKEÄ HAKEMAAN LOCATION
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10));
            }
        });

         */
    }


    private void getMarkers(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            markers = response.getJSONArray("locations");
                            addMarkers(markers);
                        }
                        catch(JSONException e) {
                            Log.d("mytag", "" + e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                    }
                });
        Log.d("mytag", "" + jsonObjectRequest);
        requestQueue.add(jsonObjectRequest);
    }

    private void addMarkers(JSONArray json) {
        markersList = new ArrayList<LatLng>();
        markerIds = new ArrayList<Integer>();

        // Parse JSONObject to arraylist
        for(int i = 0; i < json.length(); i++) {
            try {
                markerObject = json.getJSONObject(i);
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            try {
                double lat = markerObject.getDouble("lat");
                double lng = markerObject.getDouble("lng");
                LatLng coordinates = new LatLng(lat, lng);
                markerIds.add(markerObject.getInt("storyId"));
                markersList.add(coordinates);
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
        }

        // Add markers to map
        for (int i = 0; i < markersList.size(); i++) {
            marker = mMap.addMarker(
                    new MarkerOptions().
                            position(markersList.get(i)).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_small)).
                            title("Marker" + i));
        }
    }

    private class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        String imageUri = "https://i.imgur.com/tGbaZCY.jpg";

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

            Picasso.get()
                    .load(imageUri)
                    .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                    .into(image);

            //getInfoContents(marker);

            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent viewStoryIntent = new Intent(MapsActivity.this, ViewStoryActivity.class);
                    viewStoryIntent.putExtra("imagePath", imageUri);
                    startActivity(viewStoryIntent);
                }
            });

            return view;
        }
    }

    public void openDialogNoPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ssdsdsdsds")
                .setMessage("Tämdsdsdsdsse")
                .setPositiveButton("OKe vittu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.create();
    }

    public void checkPermission(int requestCode) {
        if (requestCode == ACCESS_LOCATION_CODE) {
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                    ACCESS_LOCATION_CODE);
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
                Toast.makeText(MapsActivity.this,"LOCATION PERMISSION NOT GRANTED", Toast.LENGTH_LONG).show();
                openDialogNoPermission();
            }
        }
    }
}
