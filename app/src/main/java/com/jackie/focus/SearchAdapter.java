package com.jackie.focus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    // Activity-related variables.
    private Context _context;
    private View _view;

    // Represents the purchases to be used for RecyclerView.
    private ArrayList<Locations> _locations;

    // SQL-Database variables.
    private LocationsDatabaseHelper _dbHelper;
    private SQLiteDatabase _db;

    public SearchAdapter(Context context, ArrayList<Locations> locations, View v) {
        _context = context;
        _view = v;
        _locations = new ArrayList<>(locations);

        // SQL Database Insertion
        _dbHelper = new LocationsDatabaseHelper(context);
        // Get the database. If it does not exist, this is where it will
        // also be created.
        _db = _dbHelper.getWritableDatabase();

    }

    /** Adds location L to _LOCATIONS if it is not a duplicate.
     * @param l: Location to be added. */
    public void addLocation(Locations l) {
        // Checks if location exists already. Prevents adding duplicates.
        for (Locations location : _locations) {
            double lat = location.getLat();
            double lon = location.getLong();
            if (l.getLat() == lat && l.getLong() == lon) {
                Toast.makeText(_context, "FOCUS Location already exists!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // Adds location if not duplicate and updates the view.
        _locations.add(l);
        Toast.makeText(_context, l.getName() + " has been added!", Toast.LENGTH_SHORT).show();
        ContentValues values = Utils.insertEntries(l.getID(), l.getName(), l.getAddress(), l.getLat(), l.getLong());
        long newRowId = _db.insert(LocationsDatabase.LocationEntry.TABLE_NAME, null, values);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(_context).inflate(R.layout.search_item, viewGroup, false);
        Utils.updateView(_view.findViewById(R.id.mainLayout), _locations);
        return new SearchViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.SearchViewHolder SearchViewHolder, int i) {
        SearchViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return _locations.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView _locName;
        private TextView _locAddress;
        private ImageButton _deleteLoc;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            _locName = itemView.findViewById(R.id.locName);
            _locAddress = itemView.findViewById(R.id.locAddress);
            _deleteLoc = itemView.findViewById(R.id.deleteLocation);
        }

        public void bind(int position) {
            Locations l = _locations.get(position);
            _locName.setText(l.getName());
            _locAddress.setText(l.getAddress());
            _deleteLoc.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.deleteLocation:
                    // remove the item from recycler view
                    removeItem(getAdapterPosition());
            }
        }
    }
    /** Handles removing a purchase at POSITION.
     * Removes it from the SQL Database and the RecyclerView. */
    public void removeItem(int position) {
        Locations l = _locations.get(position);
        _locations.remove(position);
        String whereClause = "placeid=?";
        String[] whereArg = new String[] {String.valueOf(l.getID())};
        _db.delete(LocationsDatabase.LocationEntry.TABLE_NAME, whereClause, whereArg);
        // notify the item removed by position
        // to perform recycler view delete animations
        notifyItemRemoved(position);
        Utils.updateView(_view, _locations);
    }
}
