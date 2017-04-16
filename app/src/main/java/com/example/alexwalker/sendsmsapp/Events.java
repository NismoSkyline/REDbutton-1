package com.example.alexwalker.sendsmsapp;

/**
 * Created by alexwalker on 15.04.17.
 */

public class Events {
    private double lat;
    private double lng;

    public Events() {
    }

    public Events(double lat, double lng) {
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
