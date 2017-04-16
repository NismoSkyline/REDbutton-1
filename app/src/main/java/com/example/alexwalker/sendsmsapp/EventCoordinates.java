package com.example.alexwalker.sendsmsapp;

/**
 * Created by alexwalker on 15.04.17.
 */

public class EventCoordinates {
    private double lat;
    private double lng;

    public EventCoordinates() {
    }

    public EventCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }
}
