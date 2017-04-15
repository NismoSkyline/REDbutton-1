package com.example.alexwalker.sendsmsapp;

/**
 * Created by alexwalker on 15.04.17.
 */

class MessageData {
    private String firstPhoneNumber;
    private String secondPhoneNumber;
    private String messageText;

    public MessageData() {
    }



    public void setData(String firstNumber, String secondNumber, String message) {
        firstPhoneNumber = firstNumber;
        secondPhoneNumber = secondNumber;
        messageText = message;
    }

    public String getFirstPhoneNumber() {
        return firstPhoneNumber;
    }

    public String getSecondPhoneNumber() {
        return secondPhoneNumber;
    }

    public String getMessageText() {
        return messageText;
    }
}
