package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class loginActivity extends AppCompatActivity {

    Button login, signUp;
    EditText editUsername, editPassword;

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
                if(editUsername.getText().toString().equals("root") &&
                        editPassword.getText().toString().equals("root")) {
                    Toast.makeText(getApplicationContext(),
                            "Redirecting...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}