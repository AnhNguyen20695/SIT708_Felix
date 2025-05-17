package com.example.lostandfoundv2.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.lostandfoundv2.DatabaseHelper;
import com.example.lostandfoundv2.MainActivity;
import com.example.lostandfoundv2.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CreateNewActivity extends AppCompatActivity {
    private final int FINE_PERMISSION_CODE = 1;
    private GoogleMap myMap;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;

    RadioButton lostButton, foundButton;
    RadioGroup postTypeGroup;
    EditText nameField, phoneField, descField, reportedDateField;
    String postType;
    Button saveButton;
    Button getCurrentLocationButton;
    private DatabaseHelper database;
    String locationName, locationLat, locationLng, locationID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new);
        initWidgets();
        database = new DatabaseHelper(this);

        postType = "lost";

        postTypeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = (RadioButton) findViewById(checkedId);
                String postTypeChecked = checkedRadioButton.getText().toString();
                Log.i("MAIN_LOG","Updated radio group.");

                if (postTypeChecked.equals("Lost")) {
                    Log.i("MAIN_LOG","Lost.");
                    postType = "lost";
                } else if (postTypeChecked.equals("Found")) {
                    Log.i("MAIN_LOG","Found.");
                    postType = "found";
                } else {postType = "not_implemented";}
            }
        });

        try {
            // Initialize the Google Place AutoComplete SDK
            ApplicationInfo app = getApplicationContext().getPackageManager().getApplicationInfo(getApplicationContext().getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = app.metaData;
            Places.initialize(getApplicationContext(), bundle.getString("com.google.android.geo.API_KEY"));
            Log.i("MAIN_LOG","Pulled Google API Key: "+bundle.getString("com.google.android.geo.API_KEY"));
        } catch (Exception e) {
            Log.i("MAIN_LOG","Error in getting metadata: "+e);
        }

        // Create a new Places client instance
        locationName = "";
        locationLat = "";
        locationLng = "";
        locationID = "";
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setTypeFilter(TypeFilter.ADDRESS);
        autocompleteFragment.setLocationBias(RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596)
        ));
        autocompleteFragment.setCountries("AU");

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Add a PlaceSelectionListener that handles the event when the user taps one of the predictions
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                Log.i("MAIN_LOG", "Place: "+place.getName()+ " | ID: "+place.getId()+" | LatLng: "+place.getLatLng());
                locationName = place.getName();
                try {
                    locationLat = String.valueOf(place.getLatLng().latitude);
                    locationLng = String.valueOf(place.getLatLng().longitude);
                } catch (Exception e) {
                    Log.i("MAIN_LOG","can not get lat lng");
                }
            }

            @Override
            public void onError(@NonNull Status status) {
                Log.i("MAIN_LOG","An error occured: "+status);
            }
        });
        // Get current location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        Geocoder geo = new Geocoder(this, Locale.getDefault());
        getCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationLat = String.valueOf(currentLocation.getLatitude());
                locationLng = String.valueOf(currentLocation.getLongitude());
                try {
                    Log.i("MAIN_LOG","Setting the location's name from the (Latitude, Longitude)= ("+locationLat+","+locationLng+")");
                    List<Address> addresses = geo.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                    if (addresses.isEmpty()) {
                        autocompleteFragment.setText("("+locationLat+","+locationLng+")");
                    } else {
                        autocompleteFragment.setText(addresses.get(0).getFeatureName() + ", " + addresses.get(0).getLocality() +", " + addresses.get(0).getAdminArea() + ", " + addresses.get(0).getCountryName());
                    }
                } catch (IOException e) {
                    Log.i("MAIN_LOG","Can not get any location's name from the (Latitude, Longitude)= ("+locationLat+","+locationLng+")");
                    autocompleteFragment.setText("");
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameField.getText().toString();
                String phoneNumber = phoneField.getText().toString();
                String description = descField.getText().toString();
                String reportedDate = reportedDateField.getText().toString();
                String location = locationName;
                Log.i("MAIN_LOG","Adding items.");
                if (postType.equals("lost")) {
                    Log.i("MAIN_LOG","Adding lost items.");
                    String result = database.addToLostItems(name,
                            phoneNumber,
                            description,
                            reportedDate,
                            location,
                            locationLat,
                            locationLng);
                    Log.i("MAIN_LOG","Lost items result: "+result);
                } else if (postType.equals("found")) {
                    Log.i("MAIN_LOG","Adding found items.");
                    String result = database.addToFoundItems(name,
                            phoneNumber,
                            description,
                            reportedDate,
                            location,
                            locationLat,
                            locationLng);
                    Log.i("MAIN_LOG","Found items result: "+result);
                }

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initWidgets() {
        lostButton = findViewById(R.id.lost_button);
        foundButton = findViewById(R.id.found_button);
        postTypeGroup = findViewById(R.id.post_type_group);
        saveButton = findViewById(R.id.save_button);
        getCurrentLocationButton = findViewById(R.id.get_current_location_button);
        nameField = findViewById(R.id.name_field);
        phoneField = findViewById(R.id.phone_field);
        descField = findViewById(R.id.description_field);
        reportedDateField = findViewById(R.id.date_field);
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null)  {
                    currentLocation = location;
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission is denied, please allow the permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}