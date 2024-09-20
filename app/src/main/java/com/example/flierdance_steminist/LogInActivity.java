package com.example.flierdance_steminist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.gbuttons.GoogleSignInButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LogInActivity extends AppCompatActivity {
    EditText etUsername, etPasswd, etEmail;
    DatabaseReference users;
    FirebaseDatabase db;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    GoogleSignInButton googleBtn;
    GoogleSignInOptions gOptions;
    GoogleSignInClient gClient;
    Auth auth1 = new Auth(this);
    String key;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        etUsername = findViewById(R.id.login_username);
        etPasswd = findViewById(R.id.login_password);
        Button btn = findViewById(R.id.login_button);
        googleBtn = findViewById(R.id.googleBtn);
        TextView btnSignUp = findViewById(R.id.signUpRedirectText);
        TextView btnForgotPassword = findViewById(R.id.forgot_password);

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        users = db.getReference().child("Users");

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameStr = etUsername.getText().toString();
                String passwdStr = etPasswd.getText().toString();

                Auth.getUserByUsername(usernameStr, new UserDataListener() {
                    @Override
                    public void OnUserDataReady(User user) {
                        if (passwdStr.isEmpty() || usernameStr.isEmpty()) {
                            Toast.makeText(LogInActivity.this, "Please fill out all fields", Toast.LENGTH_LONG).show();
                        }
                        else if (user != null && user.getPasswd().equals(passwdStr) && user.getUsername().equals(usernameStr)) {

                            Auth auth = new Auth(getApplicationContext());
                            Auth.setCurrentUser(user);
                            auth.saveKey(user.getKey());
                            Auth.saveKey(user.getKey(), getApplicationContext());

                            Intent intent = new Intent(getApplicationContext(),
                                    MainActivity.class);
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("username", usernameStr);
                            intent.putExtra("password", passwdStr);
                            intent.putExtra("key", user.getKey());

                            Toast.makeText(LogInActivity.this, "You logged in successfully", Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        } else if (user == null) {
                            Toast.makeText(LogInActivity.this, "This user does not exist. Please register first", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LogInActivity.this, "Incorrect username, email or password", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogInActivity.this, SignUpActivity.class));
            }
        });

        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LogInActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.forgot_password, null);
                EditText emailBox = dialogView.findViewById(R.id.emailBox);
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String userEmail = emailBox.getText().toString();
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(LogInActivity.this, "Enter your registered email id", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        auth.sendPasswordResetEmail(userEmail)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LogInActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(LogInActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        auth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LogInActivity.this, "Check your email", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(LogInActivity.this, "Unable to send, failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow() != null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });
        gOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gClient = GoogleSignIn.getClient(this, gOptions);
        gClient.revokeAccess();
        GoogleSignInAccount gAccount = GoogleSignIn.getLastSignedInAccount(this);
        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

                            try {
                                GoogleSignInAccount account = task.getResult(ApiException.class);
                                finish();
                                auth1.getUserByEmail(account.getEmail(), new UserDataListener() {
                                    @Override
                                    public void OnUserDataReady(User user) {
                                        key = user.getKey();
                                        Auth.setCurrentUser(user);
                                        auth1.saveKey(key);
                                        auth1.saveKey(key, getApplicationContext());
                                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                        intent.putExtra("key", key);
                                        startActivity(intent);
                                        Toast.makeText(LogInActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } catch (ApiException e){
                                Toast.makeText(LogInActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = gClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });
    }
}