package com.example.mapschat;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    // Static
    private static final int REQUEST_LOCATION = 101;

    private GoogleMap map;
    private LatLng myLocation;
    private Button addPointButton;
    private ImageButton myLocationButton;
    private Dialog addPointPopUp;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private DataBaseCoordinates dataBaseCoordinates;

    private Marker marker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Finding elements
        addPointButton = findViewById(R.id.addPointButton);
        myLocationButton = findViewById(R.id.myLocationButton);

        // Pop UP
        addPointPopUp = new Dialog(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        dataBaseCoordinates = new DataBaseCoordinates(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng romania = new LatLng(45.50, 24.59);
        map.addMarker(new MarkerOptions().position(romania));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(romania, 6));
        // HERE DATABASE RELOAD
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                dataBaseCoordinates.insertDataInTableXY(latLng.latitude, latLng.longitude);
                map.addMarker(new MarkerOptions().position(latLng).title("Lat: " + latLng.latitude + " Long: " + latLng.longitude));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    public void myLocationClicked(View view) {
        // Access permission for GPS
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            return;
        }
        // Beginning a task for user last location
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        while(task.isComplete() == false) {
            System.out.println(task.isComplete());
        }
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Add to database user location
                    if(!dataBaseCoordinates.isCoordinatesInTableUserLocation(location.getLatitude(), location.getLongitude())) {
                        dataBaseCoordinates.insertDataUserLocation(location.getLatitude(), location.getLongitude());
                        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        marker = map.addMarker(new MarkerOptions().position(myLocation).title("My location"));
                    } else {
                        marker.remove();
                        dataBaseCoordinates.updateUserLocation(location.getLatitude(), location.getLongitude());
                        myLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        marker = map.addMarker(new MarkerOptions().position(myLocation).title("My location"));
                    }
                }
            }
        });
    }

    /**
     *
     * @param view
     */
    public void addPointClicked(View view) {
        // Add point activity stats
        // Elements on screen
        final TextView xCoordinatesTextView;
        final TextView yCoordinatesTextView;
        final TextView zCoordinatesTextView;
        Button saveButton;
        Button cancelButton;


        addPointPopUp.setContentView(R.layout.activity_add_point);
        // Finding elements
        xCoordinatesTextView = addPointPopUp.findViewById(R.id.xCoordinates);
        yCoordinatesTextView = addPointPopUp.findViewById(R.id.yCoordinates);
        zCoordinatesTextView = addPointPopUp.findViewById(R.id.zCoordinates);
        saveButton = addPointPopUp.findViewById(R.id.saveButton);
        cancelButton = addPointPopUp.findViewById(R.id.cancelButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataBaseCoordinates.insertDataInTableXYZ(Double.valueOf(String.valueOf(xCoordinatesTextView.getText())),
                        Double.valueOf(String.valueOf(yCoordinatesTextView.getText())),
                        Double.valueOf(String.valueOf(zCoordinatesTextView.getText())));
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointPopUp.dismiss();
            }
        });
        addPointPopUp.show();
    }
}