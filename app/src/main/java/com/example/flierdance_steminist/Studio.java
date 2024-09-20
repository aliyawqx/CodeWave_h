package com.example.flierdance_steminist;

import android.net.Uri;

import java.io.Serializable;

public class Studio implements Serializable {
    private static int CURRENT_ID = 1;
    private String key;
    private String user;
    private String nameOfCh;
    private String price;
    private String date;
    private String time;
    private String direction;
    private String contactInfo;
    private String imageUri;
    private String address;

    public Studio(){

    }

    public Studio(String nameOfCh, String address, String price, String date, String time, String contactInfo, String direction, String user){
        this.nameOfCh = nameOfCh;
        this.price = price;
        this.date = date;
        this.time = time;
        this.contactInfo = contactInfo;
        this.direction = direction;
        this.user = user;
        this.address = address;
    }

    public Studio(String nameOfChStr, String address, String priceStr, String date, String time, String contactInfoStr, String imageUri, String directionStr, String userStr) {
        this.nameOfCh = nameOfChStr;
        this.price = priceStr;
        this.date = date;
        this.time = time;
        this.contactInfo = contactInfoStr;
        this.imageUri = imageUri;
        this.direction = directionStr;
        this.user = userStr;
        this.address = address;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getNameOfCh() {
        return nameOfCh;
    }

    public void setNameOfCh(String nameOfCh) {
        this.nameOfCh = nameOfCh;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public static void setCurrentId(int currentId) {
        CURRENT_ID = currentId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}