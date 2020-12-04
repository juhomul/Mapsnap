package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewStoryActivity extends AppCompatActivity {

    ImageView image = findViewById(R.id.storyImage);
    TextView userName = findViewById(R.id.userText);
    TextView desc = findViewById(R.id.descText);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
    }
}