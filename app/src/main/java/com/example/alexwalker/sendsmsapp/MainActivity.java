package com.example.alexwalker.sendsmsapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (isPermissionGranted()) {
                    requestPermission();
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

                MessageData messageData = new MessageData(firstPhoneNumber, secondPhoneNumber, message);
                databaseReference.push().setValue(messageData);

            }
        });
    }


    private void getData() {
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

    private void requestPermission() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, 1);
    }

    private boolean isPermissionGranted() {
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
    }
}
