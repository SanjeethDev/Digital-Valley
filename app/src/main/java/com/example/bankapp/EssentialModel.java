package com.example.bankapp;

public class EssentialModel {
    int item_image;
    String item_name;
    String item_value;

    public EssentialModel(int item_image, String item_name, String item_value) {
        this.item_image = item_image;
        this.item_name = item_name;
        this.item_value = item_value;
    }

    public int getItem_image() {
        return item_image;
    }

    public String getItem_name() {
        return item_name;
    }

    public String getItem_value() {
        return item_value;
    }

}
