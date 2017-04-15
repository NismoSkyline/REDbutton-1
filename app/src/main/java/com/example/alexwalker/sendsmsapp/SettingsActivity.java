package com.example.alexwalker.sendsmsapp;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity {

    private static final String FIRST_NUMBER = "firstNumber";
    private static final String SECOND_NUMBER = "secondNumber";
    private static final String MESSAGE = "message";
    EditText firstNumberEditText;
    EditText secondNumberEditText;
    EditText messageEditText;
    Button saveSettingsButton;
    String firstNumber;
    String secondNumber;
    String message;
    SharedPreferences preferences;
    MessageData messageData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        init();
        loadDataFromPref();
        if(isPrefSaved()){
            setData(firstNumber, secondNumber, message);
        }

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNumber = firstNumberEditText.getText().toString();
                secondNumber = secondNumberEditText.getText().toString();
                message = messageEditText.getText().toString();

                saveDataInPref(firstNumber, secondNumber, message);
                if(isPrefSaved()){
                    Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_LONG).show();
                } else Toast.makeText(getApplicationContext(), "Save failed. Try again", Toast.LENGTH_LONG).show();

            }
        });

    }

    private void saveDataInPref(String firstNumber, String secondNumber, String message) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(FIRST_NUMBER, firstNumber);
        editor.putString(SECOND_NUMBER, secondNumber);
        editor.putString(MESSAGE, message);
        editor.apply();
    }

    private void setData(String firstNumber, String secondNumber, String message) {
        messageData.setData(firstNumber, secondNumber, message);
    }


    private boolean isPrefSaved() {
        if(preferences.getString(FIRST_NUMBER, "").length() != 0 &&
                preferences.getString(SECOND_NUMBER, "").length() != 0 &&
                preferences.getString(MESSAGE, "").length() != 0){
            return true;
        } else return false;
    }

    private void loadDataFromPref() {
        firstNumber = preferences.getString(FIRST_NUMBER, "");
        secondNumber = preferences.getString(SECOND_NUMBER, "");
        message = preferences.getString(MESSAGE, "");

        firstNumberEditText.setText(firstNumber);
        secondNumberEditText.setText(secondNumber);
        messageEditText.setText(message);

    }

    private void init() {
        firstNumberEditText = (EditText)findViewById(R.id.firstNumberEditText);
        secondNumberEditText = (EditText)findViewById(R.id.secondNumberEditText);
        messageEditText = (EditText)findViewById(R.id.messageEditText);
        saveSettingsButton = (Button)findViewById(R.id.saveSettingsButton);
        preferences = getPreferences(MODE_PRIVATE);
        messageData = new MessageData();
    }
}
