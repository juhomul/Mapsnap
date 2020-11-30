package com.example.group3;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignUpTabFragment extends Fragment {

    EditText editUsername, editPassword, editPassword2, editEmail;
    Button signupButton;
    String getUsernameText, getPasswordText, getPasswordConfirm, getEmailText;
    RequestQueue requestQueue;
    JSONObject jsonBody;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.sign_up_tab_fragment, container, false);

        editUsername = root.findViewById(R.id.username_signup);
        editPassword = root.findViewById(R.id.password_signup);
        editEmail = root.findViewById(R.id.email_signup);
        editPassword2 = root.findViewById(R.id.confirm_password_signup);
        signupButton = root.findViewById(R.id.signUpButton);

        TextWatcher passwordCount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() < 6) {
                    editPassword.setError("Password must be at least 6 characters");
                }
            }
        };

        editPassword.addTextChangedListener(passwordCount);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsernameText = editUsername.getText().toString();
                getPasswordText = editPassword.getText().toString();
                getPasswordConfirm = editPassword2.getText().toString();
                getEmailText = editEmail.getText().toString();

                int length = editPassword.getText().length();

                if(!isEmailValid(getEmailText)) {
                    editEmail.setError("Invalid email address");
                }

                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                else if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }
                else if(getPasswordText.equals(getPasswordConfirm) && length >= 6) {

                    requestQueue = Volley.newRequestQueue(getContext());

                    jsonBody = new JSONObject();
                    try {
                        jsonBody.put("username", getUsernameText);
                        jsonBody.put("email", getEmailText);
                        jsonBody.put("password", getPasswordText);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    register("http://100.26.132.75/user/register");
                    //pitäis saaha errori jos käyttäjänimi on jo käytössä

                }
                else if(!getPasswordText.equals(getPasswordConfirm)) {
                    editPassword2.setError("Password doesn't match");
                }
            }
        });

        return root;
    }

    private void register(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, jsonBody, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("mytag", "" + response);
                        Toast.makeText(getContext(),
                                "Sign up complete", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("mytag", "" + error);
                        NetworkResponse networkResponse = error.networkResponse;
                        if (networkResponse != null && networkResponse.data != null) {
                            String jsonError = new String(networkResponse.data);
                            Toast.makeText(getContext(),
                                    "" + jsonError, Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(getContext(),
                                "" + error, Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
