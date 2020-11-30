package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class CreateStoryActivity extends AppCompatActivity {

    Button btnSaveStory;
    EditText textDesc;
    FusedLocationProviderClient client;
    String strLat, strLong, strDesc;
    ImageView displayImageView;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        Intent intent = getIntent();
        client = LocationServices.getFusedLocationProviderClient(this);
        textDesc = findViewById(R.id.textSaveStory);
        displayImageView = findViewById(R.id.imageDisplayView);
        requestQueue = Volley.newRequestQueue(this);

        // get image path from extras and convert to bitmap
        Bitmap image = BitmapFactory.decodeFile(intent.getStringExtra("imagePath"));
        displayImageView.setImageBitmap(image);

        Log.d("CreateStory", "imagePath: " + intent.getStringExtra("imagePath"));
        Log.d("CreateStory", "Latitude: " + strLat);
        Log.d("CreateStory", "Longitude: " + strLong);

        Intent mapsIntent = new Intent(this, MapsActivity.class);

        btnSaveStory = findViewById(R.id.buttonSaveStory);
        btnSaveStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strDesc = textDesc.getText().toString();

                Bitmap scaledImage = scaleDown(image, 1920, true);
                String encodedImage = encodeImage(scaledImage);

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("title", "title");
                    jsonBody.put("desc", strDesc);
                    jsonBody.put("lat", strLat);
                    jsonBody.put("lng", strLong);
                    jsonBody.put("image", encodedImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                uploadImage("http://100.26.132.75/story", jsonBody);

                Log.d("CreateStory", "Latitude: " + strLat);
                Log.d("CreateStory", "Longitude: " + strLong);

                startActivity(mapsIntent);
                finish();
            }
        });

        getLocation();
    }

    private void getLocation() {
        //Check permission and get location when picture has been taken

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        /**
         * private final LocationListener mLocationListener = new LocationListener() {
         *     @Override
         *     public void onLocationChanged(final Location location) {
         *         //your code here
         *     }
         * };
         *
         * @Override
         * protected void onCreate(Bundle savedInstanceState) {
         *     super.onCreate(savedInstanceState);
         *
         *     mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
         *
         *     mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
         *             LOCATION_REFRESH_DISTANCE, mLocationListener);
         * }
         */

        //TÄMÄ ON LAST LOCATION EI CURRENT!!!
        client.getLastLocation().addOnSuccessListener(CreateStoryActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double userLat = location.getLatitude();
                    double userLong = location.getLongitude();

                    strLat = String.valueOf(userLat);
                    strLong = String.valueOf(userLong);
                }
            }
        });
    }

    private void uploadImage(String url, JSONObject jsonBody) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // print response on success
                        Log.d("CreateStory", "" + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // print error response codes and message
                        try {
                            Log.d("CreateStory", error + "");
                            String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                            Log.d("CreateStory", error.networkResponse.statusCode + ": " + responseBody);
                        } catch (Exception e) {
                            Log.d("CreateStory", "error responses"+e);
                        }
                    }
                })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                // authorization token
                headers.put("Authorization", "Bearer " + SaveSharedPreference.getToken(CreateStoryActivity.this));
                return headers;
            }
        };

        requestQueue.add(jsonObjectRequest);
    }

    // encode bitmap to base64
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // jpeg compression
        bm.compress(Bitmap.CompressFormat.JPEG,40, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.NO_WRAP);

        return encImage;
    }

    // resolution compression
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());
        if (ratio >= 1.0){ return realImage; }
        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }
}