package com.jackie.focus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /** View-related variables. */
    private RecyclerView _rView;
    private SearchView _searchView;
    private SearchAdapter _adapter;

    private ArrayList<Location> _locations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing variables.
        _rView = findViewById(R.id.searchRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        _rView.setLayoutManager(layoutManager);


        // Sets up toolbar.
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);


        _locations = new ArrayList<>();
        // Comment out this to see the display when we have an empty view. vvv
        populateLocations();
        // Must set adapter after _LOCATIONS is populated.
        _adapter = new SearchAdapter(this, _locations, findViewById(R.id.mainLayout));
        _rView.setAdapter(_adapter);

    }

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
