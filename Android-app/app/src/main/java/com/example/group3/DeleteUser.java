package com.example.group3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DeleteUser {
    RequestQueue requestQueue;

    public void deleteUserRequest(String delUrl, Context ctx) {
        StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, delUrl,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        Log.d("mytag", "deleteUser onResponse: " + response);
                        SaveSharedPreference.clearUser(ctx);
                        Intent intent = new Intent();
                        intent.setClass(ctx, LoginActivity.class);
                        ctx.startActivity(intent);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "deleteUser onErrorResponse: " + error);
                        String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                        Log.d("mytag", error.networkResponse.statusCode + ": " + responseBody);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  headers = new HashMap<String, String>();
                // authorization token
                headers.put("Authorization", "Bearer " + SaveSharedPreference.getToken(ctx));
                return headers;
            }

        };

        requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(deleteRequest);
    }
}
