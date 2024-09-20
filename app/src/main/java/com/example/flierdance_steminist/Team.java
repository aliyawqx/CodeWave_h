package com.example.flierdance_steminist;

import java.io.Serializable;

public class Team implements Serializable {
    private static int CURRENT_ID = 1;
    private String key;
    private String name;
    private String description;
    private String city;
    private String contactInfo;
    private int score;

    public Team(){
    }

    public Team(String name, String description, int score, String contactInfo, String city){
        this.name = name;
        this.description = description;
        this.score = score;
        this.city = city;
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public static int getCurrentId() {
        return CURRENT_ID;
    }

    public int getScore() {
        return score;
    }

    public String getCity() {
        return city;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void setCurrentId(int currentId) {
        CURRENT_ID = currentId;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

