package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout drawer;
    TextView showEmail, showUsername, profileUsername, titleTextView, subtitleTextView, usernameTextView, timestampTextView, storyAmountTextView;
    String email, username, description, title, image, postersUsername, lat, lng, isoTime;
    ListView listView;
    RequestQueue requestQueue;
    JSONArray stories;
    JSONObject story;
    //ArrayList<String> maintitle = new ArrayList<String>();
    ArrayList<String> subtitle = new ArrayList<String>();
    ArrayList<Bitmap> imgid = new ArrayList<Bitmap>();
    ArrayList<String> usernameArraylist = new ArrayList<String>();
    ArrayList<String> latitude = new ArrayList<String>();
    ArrayList<String> longitude = new ArrayList<String>();
    ArrayList<String> timestamp = new ArrayList<>();
    ArrayList<String> popUpArray = new ArrayList<>();
    CustomListView adapter;
    MyRecyclerViewAdapter feedAdapter;
    RecyclerView recyclerView;
    ListView longPressList;
    ArrayAdapter<String> popupAdapter;
    ImageView storyImage;
    Integer storiesAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        requestQueue = Volley.newRequestQueue(this);

        getStories("http://100.26.132.75/story");

        storyAmountTextView = findViewById(R.id.stories_amount);

        drawer = findViewById(R.id.drawer_layout);
        //drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
      
        email = SaveSharedPreference.getEmail(ProfileActivity.this);
        username = SaveSharedPreference.getUserName(ProfileActivity.this);

        profileUsername = findViewById(R.id.profile_username);
        profileUsername.setText(username);

        popUpArray.add("delete story");
        popUpArray.add("asd");

        BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView = navigationView.getHeaderView(0);
        showEmail = headerView.findViewById(R.id.showEmail);
        showUsername = headerView.findViewById(R.id.showUsername);
        showEmail.setText(email);
        showUsername.setText(username);

        recyclerView = findViewById(R.id.rvFeed);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);

        feedAdapter = new MyRecyclerViewAdapter(ProfileActivity.this);
        recyclerView.setAdapter(feedAdapter);
        feedAdapter.setClickListener(new MyRecyclerViewAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.customlist); // R.layout.longpress_popup
                dialog.setTitle(null);
                //String titleString = maintitle.get(position);
                String subtitleString = subtitle.get(position);
                Bitmap imgidString = imgid.get(position);
                String usernameString = usernameArraylist.get(position);
                String timestampString = timestamp.get(position);

                storyImage = dialog.findViewById(R.id.image);
                usernameTextView = dialog.findViewById(R.id.postersUsername);
                //titleTextView = dialog.findViewById(R.id.title);
                subtitleTextView = dialog.findViewById(R.id.description);
                timestampTextView = dialog.findViewById(R.id.timestamp);

                usernameTextView.setText(usernameString);
                storyImage.setImageBitmap(imgidString);
                //titleTextView.setText(titleString);
                subtitleTextView.setText(subtitleString);
                timestampTextView.setText(timestampString);

                dialog.show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                final Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.longpress_popup); // R.layout.longpress_popup
                dialog.setTitle(null);
                longPressList = dialog.findViewById(R.id.pop_up);

                ArrayAdapter<String> popupAdapter = new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1, popUpArray);
                longPressList.setAdapter(popupAdapter);

                dialog.show();

                longPressList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String arrayText = popUpArray.get(i);
                        Toast.makeText(view.getContext(), "toimii " + arrayText + position, Toast.LENGTH_SHORT).show();
                    }
                });
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
                description = story.getString("description");
                //title = story.getString("title");
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

            OffsetDateTime odt = OffsetDateTime.parse(isoTime); //tässä matiaksen huono yritys saaha parsetettua
            String asd = odt.toString();

            if (Pattern.compile(Pattern.quote(postersUsername), Pattern.CASE_INSENSITIVE).matcher(username).find()) {

                feedAdapter.addNewItem(decodedByte);

                imgid.add(decodedByte);
                //maintitle.add(title);
                subtitle.add(description);
                usernameArraylist.add(postersUsername);
                latitude.add(lat);
                longitude.add(lng);
                timestamp.add(asd);
            }

        }
        Collections.reverse(imgid);
        //Collections.reverse(maintitle);
        Collections.reverse(subtitle);
        Collections.reverse(usernameArraylist);
        Collections.reverse(latitude);
        Collections.reverse(longitude);
        Collections.reverse(timestamp);

        feedAdapter.reverseFeed();
        storiesAmount = imgid.size();
        storyAmountTextView.setText(String.valueOf(storiesAmount));
    }
}