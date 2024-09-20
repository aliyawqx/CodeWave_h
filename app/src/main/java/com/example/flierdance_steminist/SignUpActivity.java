 package com.example.flierdance_steminist;

 import android.annotation.SuppressLint;
 import android.content.Intent;
 import android.content.SharedPreferences;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;

 import com.google.firebase.database.DataSnapshot;
 import com.google.firebase.database.DatabaseError;
 import com.google.firebase.database.DatabaseReference;
 import com.google.firebase.database.FirebaseDatabase;
 import com.google.firebase.database.Query;
 import com.google.firebase.database.ValueEventListener;

 import java.util.regex.Pattern;

 public class SignUpActivity extends AppCompatActivity {

     EditText etUsername, etEmail, etPasswd, etName;
     Button btn;
     FirebaseDatabase db;
     DatabaseReference users;

     @SuppressLint("MissingInflatedId")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_sign_up);
         etUsername = findViewById(R.id.inputUsername);
         etEmail = findViewById(R.id.inputEmail);
         etPasswd = findViewById(R.id.inputPassword);
         etName = findViewById(R.id.inputName);
         btn = findViewById(R.id.btnRegister);
         TextView btnLogIn = findViewById(R.id.btnLogin);

         db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
         users = db.getReference().child("Users");

         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 String emailStr = etEmail.getText().toString();
                 String nameStr = etName.getText().toString();
                 String passwdStr = etPasswd.getText().toString();
                 String usernameStr = etUsername.getText().toString();
                 if (passwdStr.isEmpty() || nameStr.isEmpty() || usernameStr.isEmpty() || emailStr.isEmpty()) {
                     Toast.makeText(SignUpActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                 } else if(!(isValidEmailAddress(emailStr))){
                     Toast.makeText(SignUpActivity.this, "Please enter the correct e-mail", Toast.LENGTH_LONG).show();
                 }
                 else {
                     Query query = users.orderByChild("username").equalTo(usernameStr);
                     query.addListenerForSingleValueEvent(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot) {
                             if (snapshot.hasChildren()) {
                                 Toast.makeText(SignUpActivity.this,
                                         "This username is busy already",
                                         Toast.LENGTH_LONG).show();
                             } else {
                                 DatabaseReference push = users.push();
                                 String key = push.getKey();
                                 User user = new User(usernameStr, emailStr, passwdStr, nameStr, key);
                                 users.child(key).setValue(user);
                                 Auth auth = new Auth(getApplicationContext());
                                 Auth.setCurrentUser(user);
                                 auth.saveKey(user.getKey());
                                 Auth.saveKey(user.getKey(), getApplicationContext());
                                 Intent intent = new Intent(SignUpActivity.this,
                                         MainActivity.class);
                                 intent.putExtra("username", usernameStr);
                                 intent.putExtra("email", emailStr);
                                 intent.putExtra("password", passwdStr);
                                 intent.putExtra("name", nameStr);
                                 intent.putExtra("key", key);
                                 Toast.makeText(getApplicationContext(),
                                         "Successfully registered", Toast.LENGTH_LONG).show();

                                 user.setKey(key);
                                 push.setValue(user);
                                 startActivity(intent);
                                 System.out.println("started");
                             }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {
                             System.out.println("false");
                         }
                     });
                 }
             }
         });
         btnLogIn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i = new Intent(
                         SignUpActivity.this, LogInActivity.class);
                 startActivity(i);
             }
         });
     }

     public boolean isValidEmailAddress(String email) {
         String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
         Pattern p = Pattern.compile(ePattern);
         java.util.regex.Matcher m = p.matcher(email);
         return m.matches();
     }
 }
