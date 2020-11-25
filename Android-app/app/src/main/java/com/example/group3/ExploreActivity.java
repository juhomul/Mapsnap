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
import android.view.View;
import android.widget.AdapterView;
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

public class ExploreActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    TextView showEmail, showUsername;
    String email, username, description, title, image;
    RequestQueue requestQueue;
    JSONArray stories;
    JSONObject story;
    ListView listView;
    ArrayList<String> maintitle = new ArrayList<String>();
    ArrayList<String> subtitle = new ArrayList<String>();
    ArrayList<Bitmap> imgid = new ArrayList<Bitmap>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);

        requestQueue = Volley.newRequestQueue(this);
        listView = findViewById(R.id.storyListView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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

        getStories("http://100.26.132.75/story");

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
                        showEmail = findViewById(R.id.showEmail);
                        showEmail.setText(email);

                        showUsername = findViewById(R.id.showUsername);
                        showUsername.setText(username);

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
        for(int i = 1; i < json.length(); i++) {
            try {
                story = json.getJSONObject(i);
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            try {
                description = story.getString("description");
                title = story.getString("title");
                image = story.getString("image");
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

            maintitle.add(title);
            subtitle.add(description);
            imgid.add(decodedByte);
            arrayAdapt();
        }
    }
    private void arrayAdapt() {
        CustomListView adapter = new CustomListView(this, maintitle, subtitle, imgid);
        listView.setAdapter(adapter);
    }

}