package com.example.alexwalker.sendsmsapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    String firstPhoneNumber;
    String secondPhoneNumber;
    String message;
    MessageData messageData;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private LocationManager locationManager;
    double latitudeGPS, longitudeGPS;
    Button button;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10, 0, locationListenerGPS);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataFromSharedPref();
                sendAlertMessage();

                if (isLocationEnabled()) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestGPSPermission();
                    }else{
                        //Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        EventCoordinates eventCoordinates = new EventCoordinates(latitudeGPS, longitudeGPS);
                        databaseReference.push().setValue(eventCoordinates);
                    }
                } else showAlertToEnableGPS();

               // MessageData messageData = new MessageData(firstPhoneNumber, secondPhoneNumber, message);


            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void sendAlertMessage() {
        if (isSMSPermissionGranted()) {
            requestSMSPermission();
        } else {
            try {
                sendMessage(firstPhoneNumber, message);
                sendMessage(secondPhoneNumber, message);
                Toast.makeText(getApplicationContext(), "Sms sent", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Sms fail. Please try again", Toast.LENGTH_LONG).show();
                Log.v("SMS", "sms failed: " + e);
                e.printStackTrace();
            }
        }
    }


    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlertToEnableGPS() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " +
                        "use this app")
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }


    private void getDataFromSharedPref() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREF_FILE, MODE_PRIVATE);
        firstPhoneNumber = preferences.getString(Constants.FIRST_NUMBER, "");
        secondPhoneNumber = preferences.getString(Constants.SECOND_NUMBER, "");
        message = preferences.getString(Constants.MESSAGE, "");
        Log.v("data", firstPhoneNumber + "\n" + secondPhoneNumber + "\n" + message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    private void requestSMSPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
    }

    private void requestGPSPermission(){
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
    }

    private boolean isSMSPermissionGranted() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED){
            return true;
        }else return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        sendMessage(firstPhoneNumber, message);
                        sendMessage(secondPhoneNumber, message);
                        Toast.makeText(getApplicationContext(), "Sms sent", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Sms fail. Please try again", Toast.LENGTH_LONG).show();
                        Log.v("SMS", "sms failed: " + e);
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, locationListenerGPS);
                }
                break;
        }
    }


    private void sendMessage(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void init() {
        messageData = new MessageData();
        sendButton = (Button) findViewById(R.id.redButton);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Events");
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        button = (Button)findViewById(R.id.button2);
        textView = (TextView)findViewById(R.id.textView);
    }

    private LocationListener locationListenerGPS = new LocationListener() {
        public void onLocationChanged(Location location) {
            if(location.getLatitude() != 0 && location.getLongitude() != 0){
                longitudeGPS = location.getLongitude();
                latitudeGPS = location.getLatitude();
            }
            textView.setText("lat: " + latitudeGPS + "\n" + "lng: " + longitudeGPS);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
}
