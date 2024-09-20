package com.example.flierdance_steminist;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {
    String username, passwd, name, email, imageURL;
    private static int CURRENT_ID = 1;
    private String key;
    public User(String username, String email, String passwd, String name, String imageURL, String key) {
        this.username = username;
        this.passwd = passwd;
        this.email = email;
        this.name = name;
        this.imageURL = imageURL;
        this.key = key;
    }
    public User(String username, String email, String passwd, String name, String key) {
        this.username = username;
        this.passwd = passwd;
        this.email = email;
        this.name = name;
        this.key = key;
    }

    public User(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswd() {
        return passwd;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("email", email);
        result.put("username", username);
        result.put("passwd", passwd);
        result.put("name", name);
        return result;
    }

}
