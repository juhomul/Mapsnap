package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ViewStoryActivity extends AppCompatActivity {

    String username, image, description, timestamp;
    RequestQueue requestQueue;
    JSONArray storyArray;
    JSONObject story;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        requestQueue = Volley.newRequestQueue(this);

        Bundle bundle = getIntent().getExtras();

        String storyId = bundle.getString("storyid");

        //jos tulee profiilista
        getStory("http://100.26.132.75/story/id/" + storyId);
    }
    private void getStory(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            storyArray = response.getJSONArray("story");

                            story = storyArray.getJSONObject(0);
                            image = story.getString("image");
                            username = story.getString("username");
                            description = story.getString("description");
                            timestamp = story.getString("timestamp");
                            Log.d("mytag", "" + image);
                        } catch (JSONException e) {
                            Log.d("mytag", "" + e);
                            e.printStackTrace();
                        }
                        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                        ImageView imageView = findViewById(R.id.storyImage);
                        TextView userName = findViewById(R.id.userText);
                        TextView desc = findViewById(R.id.descText);

                        imageView.setImageBitmap(decodedByte);
                        userName.setText(username);
                        desc.setText(description);



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}