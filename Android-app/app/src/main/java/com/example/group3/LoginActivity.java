package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button login, signUp;
    EditText editUsername, editPassword;
    String getUsernameText, getPasswordText, token, email, username;
    RequestQueue requestQueue;
    JSONObject jsonBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        editUsername = findViewById(R.id.usernameEdit);
        editPassword = findViewById(R.id.passwordEdit);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsernameText = editUsername.getText().toString();
                getPasswordText = editPassword.getText().toString();

                jsonBody = new JSONObject();
                try {
                    jsonBody.put("username", getUsernameText);
                    jsonBody.put("password", getPasswordText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                login("http://100.26.132.75/user/login");

                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }


                /*Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String userName = extras.getString("username");
                    String passWord = extras.getString("password");
                    if(getUsernameText.equals(userName) &&
                            getPasswordText.equals(passWord)) {
                        //todo
                        Toast.makeText(getApplicationContext(),
                                "Redirecting...", Toast.LENGTH_SHORT).show();
                    }
                    else if(getUsernameText.equals(userName) &&
                            !getPasswordText.equals(passWord)) {
                        editPassword.setError("Incorrect password");
                    }
                    else if(getPasswordText.equals(passWord) &&
                            !getUsernameText.equals(userName)) {
                        editUsername.setError("Incorrect username");
                    }*/
            }

        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }
    private void login(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag", "" + response);  // printtaan vaa vastauksen
                        try {
                            token = response.getString("token"); // hakee tokenin APIsta
                            email = response.getString("email");
                            username = response.getString("username");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        CheckBox stayLoggedIn = findViewById(R.id.stayLoggedIn);

                        if(stayLoggedIn.isChecked()) {
                            SaveSharedPreference.setStayLogged(LoginActivity.this, "yes"); //jos stayLogged string olemassa, pysyy kirjautuneena
                        }

                        SaveSharedPreference.setUserName(LoginActivity.this, username); //tallentaa usernamen sharedpreferencee
                        SaveSharedPreference.setToken(LoginActivity.this, token); //tallentaa tokenin sharedpreferencee
                        SaveSharedPreference.setEmail(LoginActivity.this, email);


                        Intent mapsIntent = new Intent(LoginActivity.this, MapsActivity.class);
                        startActivity(mapsIntent);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            Toast.makeText(getApplicationContext(),
                                    "" + jsonError, Toast.LENGTH_SHORT).show();
                        }
                        /*Toast.makeText(getApplicationContext(),
                                "Password or username incorrect", Toast.LENGTH_SHORT).show();*/
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}