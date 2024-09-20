package com.example.flierdance_steminist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.flierdance_steminist.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseDatabase db;
    DatabaseReference users;
    String username;
    String key;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        String key = intent.getStringExtra("key");
        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference().child("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User user = document.getValue(User.class);
                    String key = user.getUsername();
                    if (key.equals(document.getKey())){
                        username = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FloatingActionButton btnFloatingAction = findViewById(R.id.btnFloatingAction);
        btnFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage("Do you want to create a hackathon or team")
                .setPositiveButton("Hackathon", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this, NewStudioActivity.class);
                        intent.putExtra("key", key);
                        startActivity(intent);
                    }
                }).setNegativeButton("Team", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(MainActivity.this, AddTeamActivity.class);
                                intent.putExtra("key", key);
                                startActivity(intent);
                            }
                        });
                builder.show();
            }
        });
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if(id == R.id.home){
                replaceFragment(new HomeFragment());
                return true;
            }
            else if(id == R.id.myProfile) {
                Bundle b = new Bundle();
                b.putString("key", key);
                MyProfileFragment myProfileFragment = new MyProfileFragment();
                myProfileFragment.setArguments(b);
                replaceFragment(myProfileFragment);
                return true;

            }
            return true;
        });



        Auth.getUserByKey(key, new UserDataListener() {
            @Override
            public void OnUserDataReady(User user) {
                if (user == null) {
                } else {
                    Auth.setCurrentUser(user);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.hackathons){
            replaceFragment(new HomeFragment());
            return true;
        } else if (item_id == R.id.teams){
            Intent intent = new Intent(MainActivity.this, TeamsActivity.class);
            startActivity(intent);
            return true;
        } else if (item_id == R.id.myProfile){
            Intent intent = getIntent();
            String key = intent.getStringExtra("key");
            Bundle b = new Bundle();
            b.putString("key", key);
            MyProfileFragment myProfileFragment = new MyProfileFragment();
            myProfileFragment.setArguments(b);
            replaceFragment(myProfileFragment);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}