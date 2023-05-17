package com.dev.digitalvalley;

import com.google.firebase.Timestamp;

public class Transactions {
    Timestamp date;
    String sender;
    String receiver;
    int amount;

    public Transactions() {

    }

    public Transactions(Timestamp date, String sender, String receiver, int amount) {
        this.date = date;
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }
}
