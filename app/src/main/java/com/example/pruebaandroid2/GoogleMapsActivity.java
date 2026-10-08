package com.example.pruebaandroid2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private final LatLng defaultLocation = new LatLng(-33.498992, -70.616743);
    private List<Marker> dynamicMarkers = new ArrayList<>();
    private Marker fixedMarker2 = null;
    private Marker fixedMarker3 = null;
    private Marker userLocationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        addMarker2(null);
        addMarker3(null);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                addMarker(latLng);
            }
        });

        checkLocationPermissionAndFetchLocation();
    }

    private void addMarker(LatLng latLng) {
        if (dynamicMarkers.size() >= 1) {
            Marker oldestMarker = dynamicMarkers.get(0);
            oldestMarker.remove();
            dynamicMarkers.remove(0);
        }

        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.images);
        Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmapOriginal, 100, 100, false);

        Marker newMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Punto dinámico")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapAchicado)));
        dynamicMarkers.add(newMarker);
    }

    private void addMarker2(LatLng latLng) {
        if (fixedMarker2 != null) {
            return;
        }

        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.images);
        Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmapOriginal, 100, 100, false);

        fixedMarker2 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-33.498688, -70.616103))
                .title("Punto fijo 2")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapAchicado)));
    }

    private void addMarker3(LatLng latLng) {
        if (fixedMarker3 != null) {
            return;
        }

        Bitmap bitmapOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.images);
        Bitmap bitmapAchicado = Bitmap.createScaledBitmap(bitmapOriginal, 100, 100, false);

        fixedMarker3 = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(-33.498862, -70.615650))
                .title("Punto fijo 3")
                .icon(BitmapDescriptorFactory.fromBitmap(bitmapAchicado)));
    }

    private void checkLocationPermissionAndFetchLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

                        if (userLocationMarker != null) {
                            userLocationMarker.remove();
                        }

                        Bitmap bitmapUsuarioOriginal = BitmapFactory.decodeResource(getResources(), R.drawable.imagesyo);
                        Bitmap bitmapUsuarioAchicado = Bitmap.createScaledBitmap(bitmapUsuarioOriginal, 100, 100, false);

                        userLocationMarker = mMap.addMarker(new MarkerOptions()
                                .position(currentLatLng)
                                .title("Mi ubicación actual")
                                .icon(BitmapDescriptorFactory.fromBitmap(bitmapUsuarioAchicado)));

                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f));
                    } else {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f));
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15f));
            }
        }
    }
}