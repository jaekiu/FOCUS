package com.jackie.focus;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;

import java.util.ArrayList;

public class GeofencingHandler {
    private final int GEOFENCE_RADIUS = 125;
    private ArrayList<Geofence> _geofenceList;
    private PendingIntent _geofencePendingIntent;
    private Context _context;

    public GeofencingHandler(Context c) {
        _geofenceList = new ArrayList<>();
        _geofencePendingIntent = null;
        _context = c;
    }

    public ArrayList<Geofence> getGeofences() {
        return _geofenceList;
    }

    public void populateGeofences(ArrayList<Locations> locations) {
        for (Locations l : locations) {
            addGeofence(l);
        }
    }

    public void addGeofence(Locations l) {
        _geofenceList.add(createGeofence(l));
    }

    public Geofence createGeofence(Locations l) {
        return new Geofence.Builder()
                // Set the request ID of the geofence. This is a string to identify this
                // geofence.
                .setRequestId(l.getID())
                .setCircularRegion(
                        l.getLat(),
                        l.getLong(),
                        GEOFENCE_RADIUS
                )
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(_geofenceList);
        return builder.build();
    }

    public PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (_geofencePendingIntent != null) {
            return _geofencePendingIntent;
        }
        Intent intent = new Intent(_context.getApplicationContext(), GeofenceTransitionsIntentService.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when
        // calling addGeofences() and removeGeofences().
        _geofencePendingIntent = PendingIntent.getService(_context, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
        return _geofencePendingIntent;
    }
}
