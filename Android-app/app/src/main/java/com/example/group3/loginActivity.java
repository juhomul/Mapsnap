package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    Button login, signUp;
    EditText editUsername, editPassword;
    String getUsernameText, getPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.loginButton);
        signUp = findViewById(R.id.signUpButton);
        editUsername = findViewById(R.id.usernameEdit);
        editPassword = findViewById(R.id.passwordEdit);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUsernameText = editUsername.getText().toString();
                getPasswordText = editPassword.getText().toString();

                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }

                Bundle extras = getIntent().getExtras();
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
                    }
                }

            }

        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signUpIntent = new Intent(loginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);
            }
        });
    }

}