package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewStoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);

        Bundle bundle = getIntent().getExtras();
        String imagePath = bundle.getString("imagePath");

        ImageView image = findViewById(R.id.storyImage);
        TextView userName = findViewById(R.id.userText);
        TextView desc = findViewById(R.id.descText);

        Picasso.get()
                .load(imagePath)
                .error(R.mipmap.ic_launcher) // will be displayed if the image cannot be loaded
                .into(image);
    }
}