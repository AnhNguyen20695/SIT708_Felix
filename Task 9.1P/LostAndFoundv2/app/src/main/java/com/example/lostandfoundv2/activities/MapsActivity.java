package com.example.lostandfoundv2.activities;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.lostandfoundv2.DatabaseHelper;
import com.example.lostandfoundv2.R;
import com.example.lostandfoundv2.models.FoundModel;
import com.example.lostandfoundv2.models.LostModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostandfoundv2.databinding.ActivityMapsBinding;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private DatabaseHelper database;
    List<LostModel> lostItems;
    List<FoundModel> foundItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = new DatabaseHelper(this);
        lostItems = database.retrieveLostItems();
        foundItems = database.retrieveFoundItems();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        if (lostItems.isEmpty() && foundItems.isEmpty()) {
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        } else {
            if (!lostItems.isEmpty()) {
                for (int i = 0; i < lostItems.size(); i++) {
                    LostModel lostItem = lostItems.get(i);
                    LatLng lostItemLocation = new LatLng(Double.parseDouble(lostItem.getLocationLat()), Double.parseDouble(lostItem.getLocationLng()));
                    mMap.addMarker(new MarkerOptions().position(lostItemLocation).title(lostItem.getName()));
                    if (i == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(lostItemLocation));
                    }
                }
            }
            if (!foundItems.isEmpty()) {
                for (int i = 0; i < foundItems.size(); i++) {
                    FoundModel foundItem = foundItems.get(i);
                    LatLng foundItemLocation = new LatLng(Double.parseDouble(foundItem.getLocationLat()), Double.parseDouble(foundItem.getLocationLng()));
                    mMap.addMarker(new MarkerOptions().position(foundItemLocation).title(foundItem.getName()));
                    if (i == 0) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(foundItemLocation));
                    }
                }
            }
        }
    }
}