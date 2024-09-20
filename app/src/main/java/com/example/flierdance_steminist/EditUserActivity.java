package com.example.flierdance_steminist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditUserActivity extends AppCompatActivity {

    EditText editName, editEmail, editUsername, editPassword;
    Button saveButton;
    String nameUser, emailUser, usernameUser, passwordUser;
    FirebaseDatabase db;
    DatabaseReference users;
    String key;
    Boolean temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference().child("Users");
        users.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    User user = document.getValue(User.class);
                    if (key.equals(document.getKey())){
                        usernameUser = user.getUsername();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        saveButton = findViewById(R.id.saveButton);

        showData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean username = isUsernameChanged();
                boolean email = isEmailChanged();
                boolean name = isNameChanged();
                boolean password = isPasswordChanged();
                if (username || email || name || password) {
                    Intent i = new Intent(EditUserActivity.this, MainActivity.class);
                    i.putExtra("key", key);
                    startActivity(i);
                    Toast.makeText(EditUserActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                } else if(usernameUser.isEmpty() || nameUser.isEmpty() || emailUser.isEmpty() || passwordUser.isEmpty()){
                    Toast.makeText(EditUserActivity.this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(EditUserActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isUsernameChanged(){
        if (!usernameUser.equals(editUsername.getText().toString())){
            users.child(key).child("username").setValue(editUsername.getText().toString());
            usernameUser = editUsername.getText().toString();
            return true;
        } else{
            return false;
        }
    }
//    public boolean isUsernameUnique(){
//        temp = true;
//        if (!usernameUser.equals(editUsername.getText().toString())){
//            Query query = users.orderByChild("username").equalTo(String.valueOf(editUsername));
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.hasChild(String.valueOf(editUsername))) {
//                        Toast.makeText(getApplicationContext(),
//                                "This username is busy",
//                                Toast.LENGTH_LONG).show();
//                        temp = false;
//                    } else if (!snapshot.hasChild(usernameUser)) {
//                        if (!usernameUser.equals(editUsername.getText().toString())) {
//                            users.child(key).child("username").setValue(editUsername.getText().toString());
//                            usernameUser = editUsername.getText().toString();
//                            temp = true;
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//            return temp;
//        } else {
//            return true;
//        }
//    }

//    public boolean isUsernameUnique(){
//        Query query = users.orderByChild("username").equalTo(String.valueOf(editUsername));
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (isUsernameChanged()) {
//                    if (snapshot.hasChild(usernameUser)) {
//                        Toast.makeText(getApplicationContext(),
//                                "This username is busy",
//                                Toast.LENGTH_LONG).show();
//                        temp = false;
//                    } else {
//                        if (!usernameUser.equals(editUsername.getText().toString())) {
//                            users.child(key).child("username").setValue(editUsername.getText().toString());
//                            usernameUser = editUsername.getText().toString();
//                            temp = true;
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//        return temp;
//    }


    public boolean isEmailChanged(){
        if (!emailUser.equals(editName.getText().toString())){
            users.child(key).child("email").setValue(editEmail.getText().toString());
            emailUser = editEmail.getText().toString();
            return true;
        } else{
            return false;
        }
    }
    
    public boolean isNameChanged(){
        if (!nameUser.equals(editName.getText().toString())){
            users.child(key).child("name").setValue(editName.getText().toString());
            nameUser = editName.getText().toString();
            return true;
        } else{
            return false;
        }
    }


    public boolean isPasswordChanged(){
        if (!passwordUser.equals(editPassword.getText().toString())){
            users.child(key).child("passwd").setValue(editPassword.getText().toString());
            passwordUser = editPassword.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public void showData(){
        Intent intent = getIntent();

        nameUser = intent.getStringExtra("name");
        emailUser = intent.getStringExtra("email");
        usernameUser = intent.getStringExtra("username");
        passwordUser = intent.getStringExtra("password");

        editName.setText(nameUser);
        editEmail.setText(emailUser);
        editUsername.setText(usernameUser);
        editPassword.setText(passwordUser);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }
}