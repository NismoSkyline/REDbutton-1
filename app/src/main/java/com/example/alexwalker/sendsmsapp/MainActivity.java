package com.example.alexwalker.sendsmsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText phoneNumber;
    EditText message;
    Button sendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    private void init() {
        phoneNumber = (EditText)findViewById(R.id.phoneNumberEditText);
        message = (EditText)findViewById(R.id.messageEditText);
        sendButton = (Button)findViewById(R.id.sendButton);
    }
}
