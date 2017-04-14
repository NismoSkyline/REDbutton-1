package com.example.alexwalker.sendsmsapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumberEditText;
    EditText messageEditText;
    Button sendButton;
    String phoneNumber;
    String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = phoneNumberEditText.getText().toString();
                message = messageEditText.getText().toString();
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    try{
                        sendMessage(phoneNumber, message);
                        Toast.makeText(getApplicationContext(), "Sms sent", Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), "Sms fail. Please try again", Toast.LENGTH_LONG).show();
                        Log.v("SMS", "sms failed: " + e);
                        e.printStackTrace();
                    }
                } else ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},1);


            }
        });

    }

    private void sendMessage(String phoneNumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, message, null, null);
    }

    private void init() {
        phoneNumberEditText = (EditText)findViewById(R.id.phoneNumberEditText);
        messageEditText = (EditText)findViewById(R.id.messageEditText);
        sendButton = (Button)findViewById(R.id.sendButton);
    }
}
