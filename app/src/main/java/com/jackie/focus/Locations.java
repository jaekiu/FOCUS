package com.jackie.focus;

public class Locations {
    private String _id;
    private String _name;
    private String _address;
    private double _lat;
    private double _long;

    public Locations(String id, String name, String address, double lat, double lon) {
        _id = id;
        _name = name;
        _address = address;
        _lat = lat;
        _long = lon;
    }

    /** Returns the name of the location. */
    public String getID() {
        return _id;
    }

    /** Returns the name of the location. */
    public String getName() {
        return _name;
    }

    /** Returns the address of the location. */
    public String getAddress() {
        return _address;
    }

    /** Returns the latitude of the location. */
    public double getLat() {
        return _lat;
    }

    /** Returns the longitude of the location. */
    public double getLong() {
        return _long;
    }
}
