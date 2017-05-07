package kg.kloop.android.redbutton;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by alexwalker on 15.04.17.
 */

public class Event {
    private User user;
    private LatLng coordinates;

    public Event() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LatLng getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(LatLng coordinates) {
        this.coordinates = coordinates;
    }
}
