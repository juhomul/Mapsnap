package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    TextView showEmail, showUsername, profileUsername, subtitleTextView, usernameTextView, timestampTextView, storyAmountTextView;
    String email, username, description, image, postersUsername, lat, lng, isoTime, URL, userId, storyId;
    RequestQueue requestQueue;
    JSONArray stories;
    JSONObject story;
    ArrayList<String> storyIdlist = new ArrayList<String>();
    ArrayList<String> subtitle = new ArrayList<String>();
    ArrayList<Bitmap> imgid = new ArrayList<Bitmap>();
    ArrayList<String> usernameArraylist = new ArrayList<String>();
    ArrayList<String> latitude = new ArrayList<String>();
    ArrayList<String> longitude = new ArrayList<String>();
    ArrayList<String> timestamp = new ArrayList<String>();
    MyRecyclerViewAdapter feedAdapter;
    RecyclerView recyclerView;
    Integer storiesAmount;
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userId = SaveSharedPreference.getUserId(getApplicationContext());
        URL = "http://100.26.132.75/story/userid/" + userId;

        requestQueue = Volley.newRequestQueue(this);

        getStories(URL);

        storyAmountTextView = findViewById(R.id.stories_amount);

        drawer = findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      
        email = SaveSharedPreference.getEmail(ProfileActivity.this);
        username = SaveSharedPreference.getUserName(ProfileActivity.this);

        profileUsername = findViewById(R.id.profile_username);
        profileUsername.setText(username);

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        showEmail = headerView.findViewById(R.id.showEmail);
        showUsername = headerView.findViewById(R.id.showUsername);
        showEmail.setText(email);
        showUsername.setText(username);

        recyclerView = findViewById(R.id.rvFeed);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        feedAdapter = new MyRecyclerViewAdapter(ProfileActivity.this);
        feedAdapter.setHasStableIds(true);

        recyclerView.setAdapter(feedAdapter);
        feedAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String storyIdString = storyIdlist.get(position);

                Intent viewStoryIntent = new Intent(ProfileActivity.this, ViewStoryActivity.class);
                viewStoryIntent.putExtra("storyid", storyIdString);
                startActivity(viewStoryIntent);
                //finish();

            }

            @Override
            public void onItemLongClick(View view, int position) {
                String storyIdfromList = storyIdlist.get(position);

                ImageView image = new ImageView(getApplicationContext());
                Bitmap originalPic = imgid.get(position);
                Bitmap newSize = Bitmap.createScaledBitmap(originalPic, 400, 400, false);
                image.setImageBitmap(newSize);

                builder = new AlertDialog.Builder(ProfileActivity.this);
                builder.setMessage("Do you want to delete this story?")
                        .setCancelable(true)
                        .setView(image)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteStory("http://100.26.132.75/story/id/" + storyIdfromList);
                                imgid.remove(position); // poistaa kuvan listasta
                                feedAdapter.notifyItemRemoved(position); //päivittää listan, mutta tulee duplicate emt miks

                                storiesAmount = imgid.size();
                                storyAmountTextView.setText(String.valueOf(storiesAmount));

                                finish(); //jostain syystä tulee duplicate tonne listaan jos ei käynnistä uuellee activityä
                                startActivity(getIntent());
                                overridePendingTransition(0, 0);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });




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
                        SaveSharedPreference.clearUser(ProfileActivity.this);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.side_delete_user:
                        new AlertDialog.Builder(ProfileActivity.this)
                                .setTitle("Are you sure")
                                .setMessage("Your account and stories will be permanently deleted")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        DeleteUser deleteUser = new DeleteUser();
                                        deleteUser.deleteUserRequest("http://100.26.132.75/user/id/" + SaveSharedPreference.getUserId(ProfileActivity.this), ProfileActivity.this);
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

                    case R.id.camera:
                        startActivity(new Intent(getApplicationContext(), cameraActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.explore:
                        startActivity(new Intent(getApplicationContext(), ExploreActivity.class));
                        finish();
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profile:
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
                storyId = story.getString("storyid");
                description = story.getString("description");
                image = story.getString("image");
                postersUsername = story.getString("username");
                lat = story.getString("lat");
                lng = story.getString("lng");
                isoTime = story.getString("timestamp"); //tässä haetaan timestamp ISO 8601 muodossa
            } catch (JSONException e) {
                Log.d("mytag", "" + e);
            }
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                    Instant instant = Instant.parse(isoTime);
            Date myDate = Date.from(instant);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdfDate = new SimpleDateFormat("MMM d, yyyy HH:mm");
            sdfDate.setTimeZone(java.util.TimeZone.getTimeZone("GMT"));
            String formatDateTime = sdfDate.format(myDate);

            feedAdapter.addNewItem(decodedString);


            imgid.add(decodedByte);
            storyIdlist.add(storyId);
            subtitle.add(description);
            usernameArraylist.add(postersUsername);
            latitude.add(lat);
            longitude.add(lng);
            timestamp.add(formatDateTime);


        }
        Collections.reverse(imgid);
        Collections.reverse(storyIdlist);
        Collections.reverse(subtitle);
        Collections.reverse(usernameArraylist);
        Collections.reverse(latitude);
        Collections.reverse(longitude);
        Collections.reverse(timestamp);

        feedAdapter.reverseFeed();
        storiesAmount = imgid.size();
        storyAmountTextView.setText(String.valueOf(storiesAmount));
    }

    private void deleteStory(String delUrl) {
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, delUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mytag", "deleteStory onResponse: " + response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "deleteStory onErrorResponse: " + error);
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.d("mytag", error.networkResponse.statusCode + ": " + responseBody);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String> ();
                // authorization token
                headers.put("Authorization", "Bearer " + SaveSharedPreference.getToken(ProfileActivity.this));
                return headers;
            }

        };

        requestQueue.add(deleteRequest);
    }
}