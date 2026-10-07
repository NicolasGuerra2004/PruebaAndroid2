package com.example.pruebaandroid2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnGoogleMaps = findViewById(R.id.btnGoogleMaps);
        Button btnOpenStreetMap = findViewById(R.id.btnOpenStreetMap);

        btnGoogleMaps.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GoogleMapsActivity.class);
            startActivity(intent);
        });

        btnOpenStreetMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OpenStreetMapActivity.class);
            startActivity(intent);
        });
    }
}