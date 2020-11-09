package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton, backButton;
    EditText editUsername, editPassword, editPassword2;
    String getUsernameText, getPasswordText, getPasswordConfirm;

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

                if(TextUtils.isEmpty(getUsernameText)) {
                    editUsername.setError("This cannot be empty");
                }
                else if(TextUtils.isEmpty(getPasswordText)) {
                    editPassword.setError("This cannot be empty");
                }
                else if(getPasswordText.equals(getPasswordConfirm)) {
                    Toast.makeText(getApplicationContext(),
                            "Sign up complete", Toast.LENGTH_SHORT).show();
                    Intent loginIntent = new Intent(SignUpActivity.this, loginActivity.class);
                    loginIntent.putExtra("username", getUsernameText);
                    loginIntent.putExtra("password", getPasswordText);
                    startActivity(loginIntent);
                    //todo
                }
                else {
                    editPassword2.setError("Password doesn't match");
                }





            }
        });

    }
}