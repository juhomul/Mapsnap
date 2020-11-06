package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    Button signUpButton, backButton;
    EditText editUsername, editPassword, editPassword2;
    String newUsername, newPassword, newPassword2;

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
                newUsername = editUsername.getText().toString();
                newPassword = editPassword.getText().toString();
                newPassword2 = editPassword2.getText().toString();
                if(newPassword.equals(newPassword2)) {
                    Toast.makeText(getApplicationContext(),
                            "Sign Up complete", Toast.LENGTH_SHORT).show();
                    startActivity(backIntent);
                    //todo
                }
                else{
                    Toast.makeText(getApplicationContext(),
                            "Password doesn't match", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}