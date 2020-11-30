package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

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
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    Button login, signUp;
    EditText editUsername, editPassword;
    String getUsernameText, getPasswordText, token, email, username;
    RequestQueue requestQueue;
    JSONObject jsonBody;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        requestQueue = Volley.newRequestQueue(this);

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        editUsername = findViewById(R.id.usernameEdit);
        editPassword = findViewById(R.id.passwordEdit);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(), this, tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
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
                        finish();
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