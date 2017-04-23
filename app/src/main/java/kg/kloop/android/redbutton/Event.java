package kg.kloop.android.redbutton;

/**
 * Created by alexwalker on 15.04.17.
 */

public class Event {
    private User user;
    private double lat;
    private double lng;

    public Event() {
    }

    public Event(double lat, double lng, User user) {
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

    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
    }
}
