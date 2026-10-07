package com.example.pruebaandroid2;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class OpenStreetMapActivity extends AppCompatActivity {

    private MapView map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().setUserAgentValue(getPackageName());

        setContentView(R.layout.activity_open_street_map);

        map = findViewById(R.id.mapView);
        map.setMultiTouchControls(true);
        map.getController().setZoom(15.0);
        map.getController().setCenter(new GeoPoint(-33.4372, -70.6506));
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }
}