package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class ExploreActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    TextView showEmail, showUsername;
    String email, username, description, title, image, postersUsername, lat, lng, isoTime;
    RequestQueue requestQueue;
    JSONArray stories;
    JSONObject story;
    ListView listView;
    EditText searchBar;
    ArrayList<String> maintitle = new ArrayList<String>();
    ArrayList<String> subtitle = new ArrayList<String>();
    ArrayList<Bitmap> imgid = new ArrayList<Bitmap>();
    ArrayList<String> usernameArraylist = new ArrayList<String>();
    ArrayList<String> latitude = new ArrayList<String>();
    ArrayList<String> longitude = new ArrayList<String>();
    ArrayList<String> timestamp = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);


        requestQueue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.storyListView);
        searchBar = findViewById(R.id.searchBar);


        getStories("http://100.26.132.75/story");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String lat = latitude.get(i);
                String lng = longitude.get(i);
                Intent mapsIntent = new Intent(ExploreActivity.this, MapsActivity.class);
                mapsIntent.putExtra("latitude", lat);
                mapsIntent.putExtra("longitude", lng);
                //startActivity(mapsIntent);


                //Toast.makeText(ExploreActivity.this, ""+ timestamp.get(i), Toast.LENGTH_SHORT).show();
            }
        });
        searchBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int DRAWABLE_RIGHT = 2;

                if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= (searchBar.getRight() - searchBar.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Toast.makeText(ExploreActivity.this, "toimii", Toast.LENGTH_SHORT).show();

                        return true;
                    }
                }
                return false;
            }
        });



        drawer = findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      
        email = SaveSharedPreference.getEmail(ExploreActivity.this);
        username = SaveSharedPreference.getUserName(ExploreActivity.this);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.explore);

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
                        SaveSharedPreference.clearUser(ExploreActivity.this);
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

                        if(!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(GravityCompat.START);
                        else drawer.closeDrawer(GravityCompat.END);
                        return true;

                    case R.id.map_view:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.explore:
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
    private void getStories(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            stories = response.getJSONArray("stories");
                            parseJSON(stories);
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

        requestQueue.add(jsonObjectRequest);
    }
    private void parseJSON(JSONArray json) {
        for(int i = 0; i < json.length(); i++) {
            try {
                story = json.getJSONObject(i);
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            try {
                description = story.getString("description");
                title = story.getString("title");
                image = story.getString("image");
                postersUsername = story.getString("username");
                lat = story.getString("lat");
                lng = story.getString("lng");
                isoTime = story.getString("timestamp");
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);


            maintitle.add(title);
            subtitle.add(description);
            imgid.add(decodedByte);
            usernameArraylist.add(postersUsername);
            latitude.add(lat);
            longitude.add(lng);
            timestamp.add(isoTime);;

            Collections.reverse(maintitle);
            Collections.reverse(subtitle);
            Collections.reverse(imgid);
            Collections.reverse(usernameArraylist);
            Collections.reverse(latitude);
            Collections.reverse(longitude);
            Collections.reverse(timestamp);

            arrayAdapt();
        }
    }
    private void arrayAdapt() {
        CustomListView adapter = new CustomListView(this, maintitle, subtitle, imgid, usernameArraylist, timestamp);
        listView.setAdapter(adapter);
    }

}