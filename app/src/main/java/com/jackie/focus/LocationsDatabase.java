package com.jackie.focus;

import android.provider.BaseColumns;

public class LocationsDatabase {
    private LocationsDatabase() {}

    public static class LocationEntry implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String COLUMN_PLACE_ID = "placeid";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LAT = "lat";
        public static final String COLUMN_LONG = "long";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + LocationEntry.TABLE_NAME + " (" +
                        LocationEntry._ID + " INTEGER PRIMARY KEY," +
                        LocationEntry.COLUMN_PLACE_ID + " TEXT," +
                        LocationEntry.COLUMN_NAME + " TEXT," +
                        LocationEntry.COLUMN_ADDRESS + " TEXT," +
                        LocationEntry.COLUMN_LAT + " TEXT," +
                        LocationEntry.COLUMN_LONG + " TEXT)";

        public static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME;

    }

}
