package com.example.group3;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            EasySplashScreen config = new EasySplashScreen(SplashScreenActivity.this)
                    .withFullScreen()
                    .withTargetActivity(MainActivity.class)
                    .withSplashTimeOut(2000)
                    .withBackgroundColor(Color.parseColor("#ECC700"))
                    //.withHeaderText("Header")
                    //.withFooterText("Footer")
                    //.withBeforeLogoText("Vitun mättö android projekti")
                    .withAfterLogoText("Mapsnap")
                    .withLogo(R.drawable.icon_pika);
            //config.getHeaderTextView().setTextColor(Color.WHITE);
            //config.getFooterTextView().setTextColor(Color.WHITE);
            //config.getBeforeLogoTextView().setTextColor(Color.BLACK);
            config.getAfterLogoTextView().setTextColor(Color.BLACK);
            View easySplashScreen = config.create();
            setContentView(easySplashScreen);
        }
    }