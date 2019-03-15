package com.jackie.focus;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SearchActivity extends AppCompatActivity {
    private final String apiKey = "AIzaSyAvpSiig4OH-LXYxFyznsaaK3yjhqdZxP0";
    private ArrayList<Location> _locations = new ArrayList<>();
    private RecyclerView _rView;
    private SearchAdapter _adapter;
    private GeofencingClient _geofencingClient;
    private GeofencingHandler _geofencingHandler;

    /** SQL-related variables. */
    private LocationsDatabaseHelper _dbHelper;
    private SQLiteDatabase _db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // SQL Database Insertion
        _dbHelper = new LocationsDatabaseHelper(this);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        _db = _dbHelper.getWritableDatabase();

        // Initializing variables.
        _rView = findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        _rView.setLayoutManager(layoutManager);
        _locations = new ArrayList<>();
        // Must set adapter after _LOCATIONS is populated.
        retrieveLocations();
        _geofencingHandler = new GeofencingHandler(this);
        _adapter = new SearchAdapter(this, _locations, findViewById(R.id.mainLayout));
        _rView.setAdapter(_adapter);

        _geofencingClient = LocationServices.getGeofencingClient(this);



        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.ADDRESS, Place.Field.NAME, Place.Field.LAT_LNG));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i("Autocomplete pls", "Place: " + place.getName() + ", " + place.getId());
                LatLng latLng = place.getLatLng();
                double latitude = latLng.latitude;
                double longitude = latLng.longitude;
                Location l = new Location(place.getId(), place.getName(), place.getAddress(), latitude, longitude);
                _adapter.addLocation(l);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("Autocomplete pls", "An error occurred: " + status);
            }
        });

    }

    @Override
    protected void onDestroy() {
        _dbHelper.close();
        super.onDestroy();
    }

    /** Populates _LOCATIONS array with entries in SQL Database. */
    void retrieveLocations() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                LocationsDatabase.LocationEntry.COLUMN_PLACE_ID,
                LocationsDatabase.LocationEntry.COLUMN_NAME,
                LocationsDatabase.LocationEntry.COLUMN_ADDRESS,
                LocationsDatabase.LocationEntry.COLUMN_LAT,
                LocationsDatabase.LocationEntry.COLUMN_LONG
        };

        Cursor cursor = _db.query(
                LocationsDatabase.LocationEntry.TABLE_NAME,   // The table to query
                projection,                            // The array of columns to return (pass null to get all)
                null,                         // The columns for the WHERE clause
                null,                     // The values for the WHERE clause
                null,                         // don't group the rows
                null,                          // don't filter by row groups
                null                             // The sort order
        );

        while(cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(LocationsDatabase.LocationEntry._ID));
            String placeid = cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationsDatabase.LocationEntry.COLUMN_PLACE_ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationsDatabase.LocationEntry.COLUMN_NAME));
            String address = cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationsDatabase.LocationEntry.COLUMN_ADDRESS));
            String lat = cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationsDatabase.LocationEntry.COLUMN_LAT));
            String lon = cursor.getString(
                    cursor.getColumnIndexOrThrow(LocationsDatabase.LocationEntry.COLUMN_LONG));
            Location p = new Location(placeid, name, address, Double.parseDouble(lat), Double.parseDouble(lon));
            _locations.add(p);
        }
        cursor.close();
    }

    @SuppressWarnings("MissingPermission")
    private void populateGeofences() {
        _geofencingClient.addGeofences(_geofencingHandler.getGeofencingRequest(), _geofencingHandler.getGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Geofences added
                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to add geofences
                        // ...
                    }
                });
    }
//    private void populateLocations() {
//        _locations.add(new Location("Home", "2520 College Ave.", 192.0, 290.8));
//        _locations.add(new Location("Yali's Cafe", "1920 Oxford St", 37.8734, 122.2664));
//    }
}
