package com.example.flierdance_steminist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MyProfileFragment extends Fragment {
    TextView profileEmail, profileUsername, profilePassword, profileName, delete, signOut;
    Button btnMyStudio, btnEdit;
    Auth auth = new Auth(getActivity());
    FirebaseDatabase db;
    DatabaseReference users, studios;
    String name, password, email, key, username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        key = getArguments().getString("key");

        profileName = view.findViewById(R.id.profileName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileUsername = view.findViewById(R.id.profileUsername);
        profilePassword = view.findViewById(R.id.profilePassword);
        delete = view.findViewById(R.id.delete);
        signOut = view.findViewById(R.id.signOut);
        btnMyStudio = view.findViewById(R.id.btnMyStudio);
        btnEdit = view.findViewById(R.id.btnEdit);
        showAllUserData();


        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        studios = db.getReference().child("Studios");
        users = db.getReference().child("Users");

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete your account?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
                                sharedPreferences.edit().remove("key").apply();
                                users.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot document : snapshot.getChildren()) {
                                            User u = document.getValue(User.class);
                                            users.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getActivity(), "Successfully deleted", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(getActivity(), SignUpActivity.class);
                                                        startActivity(i);
                                                    }
                                                    else{
                                                        Toast.makeText(getActivity(), "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", null);
                builder.show();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setTitle("Log out")
                        .setMessage("Are you sure you want to log out?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", getContext().MODE_PRIVATE);
                                sharedPreferences.edit().remove("key").apply();
                                Intent i = new Intent(getActivity(), SignUpActivity.class);
                                startActivity(i);
                            }
                        }).setNegativeButton("No", null);
                builder.show();
            }
        });
        btnMyStudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auth.getStudioByKeyUser(key, new StudioDataListener() {
                    @Override
                    public void OnStudioDataReady(Studio studio) {
                        if (studio == null){
                            Toast.makeText(getActivity().getApplicationContext(), "You don't have any hackathons", Toast.LENGTH_SHORT).show();
                        } else{
                            Intent intent = new Intent(getActivity(), MyStudioActivity.class);
                            intent.putExtra("keyUser", key);
                            startActivity(intent);
                        }
                    }
                });
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditUserActivity.class);
                intent.putExtra("key", key);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        return view;
        }

    public void showAllUserData() {

        Auth.getUserByKey(key, new UserDataListener() {
            @Override
            public void OnUserDataReady(User user) {
//                if (user == null){
//                    Intent intent = new Intent(getActivity(), SignUpActivity.class);
//                    Toast.makeText(getActivity(), "You haven't registered yet", Toast.LENGTH_SHORT);
//                    startActivity(intent);
//                } else {
                    name = user.getName();
                    email = user.getEmail();
                    password = user.getPasswd();
                    username = user.getUsername();
                    profileUsername.setText(username);
                    profileEmail.setText(email);
                    profilePassword.setText(password);
                    profileName.setText(name + "!");
//                }
            }
        });
    }
}