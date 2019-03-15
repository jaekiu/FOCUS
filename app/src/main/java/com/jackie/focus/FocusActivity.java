package com.jackie.focus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.ArrayList;
import java.util.Arrays;

public class FocusActivity extends AppCompatActivity {

    /** View-related variables. */
    private RecyclerView _rView;
    private SearchView _searchView;
    private SearchAdapter _adapter;

    private ArrayList<Location> _locations;
    private GeofencingClient _geofencingClient;

    private String apiKey = "AIzaSyAvpSiig4OH-LXYxFyznsaaK3yjhqdZxP0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_focus);

        // Initializing variables.
        _rView = findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        _rView.setLayoutManager(layoutManager);

        // Geofencing
        _geofencingClient = LocationServices.getGeofencingClient(this);

        // Sets up toolbar.
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        _locations = new ArrayList<>();
        // Comment out this to see the display when we have an empty view. vvv
        populateLocations();
        // Must set adapter after _LOCATIONS is populated.
        _adapter = new SearchAdapter(this, _locations, findViewById(R.id.mainLayout));
        _rView.setAdapter(_adapter);

        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        addAutoComplete();

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

    }

    public void addAutoComplete() {
        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("AutoComplete", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("AutoComplete", "An error occurred: " + status);
            }
        });
    }

    /**
     * Override the activity's onActivityResult(), check the request code, and
     * do something with the returned place data (in this example it's place name and place ID).
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        int AUTOCOMPLETE_REQUEST_CODE = 1;
//        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Place place = Autocomplete.getPlaceFromIntent(data);
//                Log.i("AutoCorrect", "Place: " + place.getName() + ", " + place.getId());
//            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
//                // TODO: Handle the error.
//                Status status = Autocomplete.getStatusFromIntent(data);
//                Log.i("AutoCorrect", status.getStatusMessage());
//            } else if (resultCode == RESULT_CANCELED) {
//                // The user canceled the operation.
//            }
//        }
//    }

    private void populateLocations() {
        _locations.add(new Location("Home", "2520 College Ave.", 192.0, 290.8));
        _locations.add(new Location("Yali's Cafe", "1920 Oxford St", 37.8734, 122.2664));
    }

    /** Creates all the menu options for the toolbar (the search button). */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.searchbar, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        _searchView = (SearchView) searchItem.getActionView();
        _searchView.setMaxWidth(Integer.MAX_VALUE);

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {

                _adapter.getFilter().filter(text);
                return false;
            }
        });

        // Will set the search query to the most recent search, if present.
        _searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String prevFilter = Utils.retrievePrevQuery(getApplicationContext());
                // _searchView.setQuery(prevFilter, false);
            }
        });

        ImageView closeButton = _searchView.findViewById(R.id.search_close_btn);

        // Set on click listener
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _searchView.setQuery(null, false);
                // Utils.resetQuery(getApplicationContext());
            }
        });

        return true;
    }

}
