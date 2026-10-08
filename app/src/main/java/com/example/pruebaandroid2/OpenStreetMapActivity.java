package com.example.pruebaandroid2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OpenStreetMapActivity extends AppCompatActivity implements MapEventsReceiver {

    private MapView map;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final GeoPoint defaultLocation = new GeoPoint(-33.498992, -70.616743);
    private List<Marker> dynamicMarkers = new ArrayList<>();
    private Marker fixedMarker2 = null;
    private Marker fixedMarker3 = null;

    private MyLocationNewOverlay myLocationOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();

        File basePath = new File(ctx.getCacheDir(), "osmdroid");
        Configuration.getInstance().setOsmdroidBasePath(basePath);
        Configuration.getInstance().setOsmdroidTileCache(new File(basePath, "tiles"));

        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Configuration.getInstance().setUserAgentValue("M/ a@gmail.com");

        setContentView(R.layout.activity_open_street_map);
        map = findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.WIKIMEDIA);
        map.setMultiTouchControls(true);
        map.getController().setZoom(20.0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
        map.getOverlays().add(0, mapEventsOverlay);

        myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        myLocationOverlay.enableMyLocation();
        map.getOverlays().add(myLocationOverlay);

        addMarker2(null);
        addMarker3(null);

        checkLocationPermissionAndFetchLocation();
    }
    private void addMarker(GeoPoint p) {
        if (dynamicMarkers.size() >= 1) {
            Marker oldestMarker = dynamicMarkers.get(0);
            map.getOverlays().remove(oldestMarker); // Lo quita de la vista de osmdroid
            dynamicMarkers.remove(0); // Lo borra de la lista de seguimiento
        }

        Marker newMarker = new Marker(map);
        newMarker.setPosition(p);
        newMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        newMarker.setTitle("Punto dinámico");

        Drawable originalDrawable = ContextCompat.getDrawable(this, R.drawable.images);
        if (originalDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) originalDrawable).getBitmap();
            Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmap, 64, 64, true);
            Drawable iconoFinal = new BitmapDrawable(getResources(), bitmapAchicado);
            newMarker.setIcon(iconoFinal);
        }

        map.getOverlays().add(newMarker);
        dynamicMarkers.add(newMarker);
        map.invalidate();
    }
    private void addMarker2(GeoPoint p) {
        if (fixedMarker2 != null) {
            return;
        }

        fixedMarker2 = new Marker(map);
        fixedMarker2.setPosition(new GeoPoint(-33.498688, -70.616103));
        fixedMarker2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        fixedMarker2.setTitle("Punto fijo 2");

        Drawable originalDrawable = ContextCompat.getDrawable(this, R.drawable.images);
        if (originalDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) originalDrawable).getBitmap();
            Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmap, 64, 64, true);
            Drawable iconoFinal = new BitmapDrawable(getResources(), bitmapAchicado);
            fixedMarker2.setIcon(iconoFinal);
        }

        map.getOverlays().add(fixedMarker2);
        map.invalidate();
    }
    private void addMarker3(GeoPoint p) {
        if (fixedMarker3 != null) {
            return;
        }

        fixedMarker3 = new Marker(map);
        fixedMarker3.setPosition(new GeoPoint(-33.498862, -70.615650));
        fixedMarker3.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        fixedMarker3.setTitle("Punto fijo 3");

        Drawable originalDrawable = ContextCompat.getDrawable(this, R.drawable.images);
        if (originalDrawable != null) {
            Bitmap bitmap = ((BitmapDrawable) originalDrawable).getBitmap();
            Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmap, 64, 64, true);
            Drawable iconoFinal = new BitmapDrawable(getResources(), bitmapAchicado);
            fixedMarker3.setIcon(iconoFinal);
        }

        map.getOverlays().add(fixedMarker3);
        map.invalidate();
    }

    private void checkLocationPermissionAndFetchLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        GeoPoint currentPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
                        map.getController().setCenter(currentPoint);
                    } else {
                        map.getController().setCenter(defaultLocation);
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkLocationPermissionAndFetchLocation();
            } else {
                Toast.makeText(this, "Permiso denegado, usando ubicación por defecto", Toast.LENGTH_SHORT).show();
                map.getController().setCenter(defaultLocation);
            }
        }
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

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint p) {
        addMarker(p);
        return true;
    }

    @Override
    public boolean longPressHelper(GeoPoint p) {
        return false;
    }
}