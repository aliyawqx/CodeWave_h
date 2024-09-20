package com.example.flierdance_steminist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class AddTeamActivity extends AppCompatActivity {

    EditText profileCity, titleName, profileContactInfo, profileDescription;
    String cityStr, nameStr, contactInfoStr, descriptionStr, userStr;
    Button doneButton;
    FirebaseDatabase db;
    DatabaseReference teams;

    int score = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_team);
        profileCity = findViewById(R.id.profileCity);
        titleName = findViewById(R.id.titleName);
        profileContactInfo = findViewById(R.id.profileContactInfo);
        profileDescription= findViewById(R.id.profileDescription);
        doneButton = findViewById(R.id.doneButton);

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        teams = db.getReference().child("Teams");

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityStr = profileCity.getText().toString();
                nameStr = titleName.getText().toString();
                contactInfoStr = profileContactInfo.getText().toString();
                descriptionStr = profileDescription.getText().toString();
                if (nameStr.isEmpty() || cityStr.isEmpty() || contactInfoStr.isEmpty() || descriptionStr.isEmpty()){
                    Toast.makeText(AddTeamActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Team team = new Team(nameStr, descriptionStr, score, contactInfoStr, cityStr);
                    DatabaseReference push = teams.push();
                    String key = push.getKey();
                    team.setKey(key);
                    push.setValue(team);
                    Intent intent = new Intent(AddTeamActivity.this, MainActivity.class);
                    userStr = intent.getStringExtra("key");
                    intent.putExtra("key", userStr);
                    Toast.makeText(AddTeamActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }
}