package com.example.alexwalker.sendsmsapp;

/**
 * Created by alexwalker on 15.04.17.
 */

public class Events {
    private Users user;
    private double lat;
    private double lng;

    public Events() {
    }

    public Events(double lat, double lng, Users user) {
        this.lat = lat;
        this.lng = lng;
        this.user = user;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Users getUser() {
        return user;
    }
    public void setUser(Users user) {
        this.user = user;
    }
}
