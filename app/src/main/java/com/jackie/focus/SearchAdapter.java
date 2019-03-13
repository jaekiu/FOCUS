package com.jackie.focus;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> implements Filterable {
    // Activity-related variables.
    private Context _context;
    private View _view;

    // Represents the purchases to be used for RecyclerView.
    private ArrayList<Location> _locations;
    private ArrayList<Location> _filteredLocations;

    // SQL-Database variables.
//    private InventoryDbHelper _dbHelper;
//    private SQLiteDatabase _db;

    public SearchAdapter(Context context, ArrayList<Location> locations, View v) {
        _context = context;
        _view = v;
        _filteredLocations = locations;
        _locations = new ArrayList<>(locations);

        // SQL Database Insertion
        // _dbHelper = new InventoryDbHelper(context);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        // _db = _dbHelper.getWritableDatabase();

    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(_context).inflate(R.layout.search_item, viewGroup, false);
        Utils.updateView(_view.findViewById(R.id.mainLayout), _filteredLocations);
        return new SearchViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder SearchViewHolder, int i) {
        SearchViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return _filteredLocations.size();
    }

    @Override
    public Filter getFilter() {
        return searchFilter;
    }

    /** Creates a search filter based on the search query. */
    private Filter searchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Location> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(_locations);
            } else {
                String prefix = constraint.toString().toLowerCase().trim();
                for (Location l : _locations) {
                    if (l.getName().toLowerCase().startsWith(prefix) || l.getName().startsWith(prefix)) {
                        filteredList.add(l);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        /** Updates RecyclerView with filtered results. */
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredLocations.clear();
            _filteredLocations.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    class SearchViewHolder extends RecyclerView.ViewHolder {
        private TextView _locName;
        private TextView _locAddress;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);

            _locName = itemView.findViewById(R.id.locName);
            _locAddress = itemView.findViewById(R.id.locAddress);
        }

        public void bind(int position) {
            Location l = _filteredLocations.get(position);
            _locName.setText(l.getName());
            _locAddress.setText(l.getAddress());
        }

    }
}
