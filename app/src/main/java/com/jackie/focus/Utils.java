package com.jackie.focus;

import android.content.ContentValues;
import android.content.Context;
import android.media.AudioManager;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import java.util.ArrayList;

public class Utils {

    /** Silences phone. */
    public static void mutePhone(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
    }

    /** Unmutes and restores phone to previous state. */
    public static void unmutePhone(Context context) {
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

    /** Updates the layout view based on whether there are items in the RecyclerView.
     * @param v: View that uses the layouts.
     * @param arr: Array used to check whether there are items to display. */
    public static <T> void updateView(View v, ArrayList<T> arr) {
        ConstraintLayout _hasItemsLayout = v.findViewById(R.id.containsItems);
        ConstraintLayout _noItemsLayout = v.findViewById(R.id.containsNoItems);
        if (arr == null || arr.size() == 0) {
            // Displays a layout that encourages the user to add items.
            _noItemsLayout.bringToFront();
        } else {
            // Displays the items.
            _hasItemsLayout.bringToFront();
        }
    }

    /** Creates a new map of values for Locations SQL Database.
     * @param placeid: ID of place.
     * @param name: Name of location.
     * @param address: Address of location.
     * @param lat: Latitude of location.
     * @param lon: Longitude of location.
     * @return: Returns one entry of the SQL Database. */
    public static ContentValues insertEntries(String placeid, String name, String address, double lat, double lon) {
        ContentValues values = new ContentValues();
        values.put(LocationsDatabase.LocationEntry.COLUMN_PLACE_ID, placeid);
        values.put(LocationsDatabase.LocationEntry.COLUMN_NAME, name);
        values.put(LocationsDatabase.LocationEntry.COLUMN_ADDRESS, address);
        values.put(LocationsDatabase.LocationEntry.COLUMN_LAT, Double.toString(lat));
        values.put(LocationsDatabase.LocationEntry.COLUMN_LONG, Double.toString(lon));
        return values;
    }
}
