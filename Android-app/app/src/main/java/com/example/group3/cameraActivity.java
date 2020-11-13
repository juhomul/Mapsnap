package com.example.group3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.camerakit.CameraKitView;

import java.io.File;
import java.io.FileOutputStream;

public class cameraActivity extends AppCompatActivity {

    private CameraKitView cameraKitView;
    private Button photoButton;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        cameraKitView = findViewById(R.id.camera);

        photoButton = findViewById(R.id.photoButton);
        photoButton.setOnClickListener(photoOnClickListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            cameraKitView.captureImage(new CameraKitView.ImageCallback() {
                @Override
                public void onImage(CameraKitView cameraKitView, byte[] photo) {
                    File savedPhoto = new File(Environment.getExternalStorageDirectory(), "photo.jpg");
                    try {
                        FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
                        outputStream.write(photo);
                        outputStream.close();
                    } catch (java.io.IOException e) {
                        e.printStackTrace();
                        Log.e("cameraActivity", "Exception in photo callback");
                    }
                }
            });
        }
    };
}