package kg.kloop.android.redbutton;

import android.Manifest;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.alexwalker.sendsmsapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import io.fabric.sdk.android.services.common.SystemCurrentTimeProvider;


public class LocationService extends Service {
    Event event;
    LocationListener locationListener;
    LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        event = new Event();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        setLocationListener();
        requestLocationUpdates();
        sendDataBack();
        /*if(event.getLat() != 0 && event.getLng() != 0){
            stopSelf();
        }
        WakefulBroadcastReceiver.completeWakefulIntent(intent);*/

        return super.onStartCommand(intent, flags, startId);
    }

    private void requestLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 5, locationListener);
        }
    }

    private void sendDataBack() {
        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
        localIntent.putExtra(Constants.EVENT_LAT, event.getLat());
        localIntent.putExtra(Constants.EVENT_LNG, event.getLng());
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(localIntent);
    }

    void setLocationListener(){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                event.setLng(location.getLongitude());
                event.setLat(location.getLatitude());
                Log.v("service", "Location: " + event.getLat() + " " + event.getLng());
                sendNotification();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

    }

    private void sendNotification() {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Coordinates")
                .setContentText("lat: " + event.getLat() + "\n" + "lng: " + event.getLng());
        int notificationID = 001;
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(notificationID, notificationBuilder.build());
    }

}
