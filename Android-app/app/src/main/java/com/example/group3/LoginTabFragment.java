package com.example.group3;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginTabFragment extends Fragment {

    Button login;
    EditText editUsername, editPassword;
    String getUsernameText, getPasswordText, token, email, username, userId;
    RequestQueue requestQueue;
    JSONObject jsonBody;
    CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment, container, false);

        login = root.findViewById(R.id.loginButton);
        editUsername = root.findViewById(R.id.usernameEdit);
        editPassword = root.findViewById(R.id.passwordEdit);
        checkBox = root.findViewById(R.id.stay_logged_in_checkbox);
        requestQueue = Volley.newRequestQueue(getContext());

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        return root;
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
                            userId = response.getString("userId");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(checkBox.isChecked()) {
                            SaveSharedPreference.setStayLogged(getContext(), "yes"); //jos stayLogged string olemassa, pysyy kirjautuneena
                        }

                        SaveSharedPreference.setUserName(getContext(), username);//tallentaa usernamen sharedpreferencee
                        SaveSharedPreference.setToken(getContext(), token); //tallentaa tokenin sharedpreferencee
                        SaveSharedPreference.setEmail(getContext(), email);
                        SaveSharedPreference.setUserId(getContext(), userId);


                        Intent mapsIntent = new Intent(getContext(), MapsActivity.class);
                        startActivity(mapsIntent);
                        getActivity().finish();
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
                        /*Toast.makeText(getApplicationContext(),
                                "Password or username incorrect", Toast.LENGTH_SHORT).show();*/
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
