package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton, backButton;
    EditText editUsername, editPassword, editPassword2, editEmail;
    String getUsernameText, getPasswordText, getPasswordConfirm, getEmailText;
    RequestQueue requestQueue;
    JSONObject jsonBody;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        Intent backIntent = new Intent(SignUpActivity.this, loginActivity.class);

        signUpButton = findViewById(R.id.signUpButton);
        backButton = findViewById(R.id.backButton);
        editUsername = findViewById(R.id.usernameEdit);
        editPassword = findViewById(R.id.passwordEdit);
        editPassword2 = findViewById(R.id.password2Edit);
        editEmail = findViewById(R.id.emailEdit);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(backIntent);
            }
        });
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsernameText = editUsername.getText().toString();
                getPasswordText = editPassword.getText().toString();
                getPasswordConfirm = editPassword2.getText().toString();
                getEmailText = editEmail.getText().toString();


                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                else if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }
                else if(getPasswordText.equals(getPasswordConfirm)) {
                    Toast.makeText(getApplicationContext(),
                            "Sign up complete", Toast.LENGTH_SHORT).show();

                    requestQueue = Volley.newRequestQueue(SignUpActivity.this);

                    jsonBody = new JSONObject();
                    try {
                        jsonBody.put("username", getUsernameText);
                        jsonBody.put("email", getEmailText);
                        jsonBody.put("password", getPasswordText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    register("http://34.203.33.211/user/register");
                    startActivity(backIntent);

                }
                else {
                    editPassword2.setError("Password doesn't match");
                }
            }
        });

    }
    private void register(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag", "" + response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                        Toast.makeText(getApplicationContext(),
                                "" + error, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}