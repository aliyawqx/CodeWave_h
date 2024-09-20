package com.example.flierdance_steminist;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Auth {
    private Context context;
    private HomeFragment homeFragment;
    private static User currentUser = null;
    private static String username = null;
    private static String currentKey = null;
    private static String key = null;
    private static Studio currentStudio = null;
    private static String studioName = null;

    public Auth(Context context) {
        this.context = context;
    }

    public Auth(HomeFragment homeFragment) {
        this.homeFragment = homeFragment;
    }

    public static Studio getCurrentStudio() {
        return currentStudio;
    }

    public static void setCurrentStudio(Studio currentStudio) {
        Auth.currentStudio = currentStudio;
    }

    public String getStudioNameFromSP() {
        if (Auth.studioName == null) {
            SharedPreferences sp = context.getSharedPreferences("data",
                    context.MODE_PRIVATE);
            studioName = sp.getString("studio name", null);
        }
        return studioName;
    }

    public void saveStudioName(String studioName) {
        Auth.studioName = studioName;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("studio name", studioName);
        edit.apply();
    }

    public static void getStudioByStoreName(String storeName, StudioDataListener listener){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference studios = db.getReference().child("Studios");

        Query query = studios.orderByChild("studioName").equalTo(storeName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio s = document.getValue(Studio.class);

                    listener.OnStudioDataReady(s);
                    return;
                }
                listener.OnStudioDataReady(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnStudioDataReady(null);
            }
        });
    }

    public static void getStudioByKeyUser(String key, StudioDataListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference studios = db.getReference().child("Studios");

        Query query = studios.orderByChild("user").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio u = document.getValue(Studio.class);

                    listener.OnStudioDataReady(u);
                    return;
                }
                listener.OnStudioDataReady(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnStudioDataReady(null);
            }
        });
    }

    public static void getStudioByKey(String key, StudioDataListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference studios = db.getReference().child("Studios");

        Query query = studios.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio u = document.getValue(Studio.class);

                    listener.OnStudioDataReady(u);
                    return;
                }
                listener.OnStudioDataReady(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnStudioDataReady(null);
            }
        });
    }

    public static void getStudioByStudioName(String studioName, StudioDataListener listener){
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference products = db.getReference().child("Studios");

        Query query = products.orderByChild("studio name").equalTo(studioName);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio s = document.getValue(Studio.class);

                    listener.OnStudioDataReady(s);
                    return;
                }
                listener.OnStudioDataReady(null);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnStudioDataReady(null);
            }
        });
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Auth.currentUser = currentUser;
    }

    public String getUsernameFromSP() {
        if (Auth.username == null) {
            SharedPreferences sp = context.getSharedPreferences("data",
                    context.MODE_PRIVATE);
            username = sp.getString("username", null);
        }
        return username;
    }

//    public static void removeKeyFromSp(String key, Context context){
//        SharedPreferences sp = context.getSharedPreferences("data",
//                    context.MODE_PRIVATE);
//        sp.edit().remove(key).apply();
//    }

    public static void saveKey(String key, Context context) {
        Auth.key = key;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("key", key);
        edit.apply();
    }

    public void saveKey(String key) {
        Auth.key = key;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("key", key);
        edit.apply();
    }

    public String getKeyFromSp(){
        if (Auth.key == null) {
            SharedPreferences sp = context.getSharedPreferences("data",
                    context.MODE_PRIVATE);
            key = sp.getString("key", null);
        }
        return key;
    }
    public void saveUsername(String username) {
        Auth.username = username;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username", username);
        edit.apply();
    }

    public static void saveUsername(String email, Context context) {
        Auth.username = email;
        SharedPreferences sp = context.getSharedPreferences("data",
                context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("username", email);
        edit.apply();
    }

    public static void getUserByUsername(String username, UserDataListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference users = db.getReference().child("Users");

        Query query = users.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User u = document.getValue(User.class);

                    listener.OnUserDataReady(u);
                    return;
                }
                listener.OnUserDataReady(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnUserDataReady(null);
            }
        });
    }

    public static void getUserByEmail(String email, UserDataListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference users = db.getReference().child("Users");

        Query query = users.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User u = document.getValue(User.class);

                    listener.OnUserDataReady(u);
                    return;
                }
                listener.OnUserDataReady(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnUserDataReady(null);
            }
        });
    }

    public static void getUserByKey(String key, UserDataListener listener) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference users = db.getReference().child("Users");

        Query query = users.orderByChild("key").equalTo(key);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User u = document.getValue(User.class);

                    listener.OnUserDataReady(u);
                    return;
                }
                listener.OnUserDataReady(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.OnUserDataReady(null);
            }
        });
    }
}
