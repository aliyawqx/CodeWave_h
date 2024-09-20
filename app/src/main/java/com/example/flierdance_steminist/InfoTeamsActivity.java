package com.example.flierdance_steminist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InfoTeamsActivity extends AppCompatActivity {

    TextView nameOfCh, address, price, time, date, contactInfo;
    Auth auth;
    Button buyBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_teams);
        nameOfCh = findViewById(R.id.nameOfCh);
        address =findViewById(R.id.address);
        price = findViewById(R.id.price);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        contactInfo = findViewById(R.id.contactInfo);

        buyBtn = findViewById(R.id.buyButton);

        auth = new Auth(this);

        Intent i = getIntent();
        String usernameStr = i.getStringExtra("username");
        Auth.getStudioByStudioName(auth.getStudioNameFromSP(), new StudioDataListener() {
            @Override
            public void OnStudioDataReady(Studio studio) {
                if (studio == null) {
                } else {
                    Auth.setCurrentStudio(studio);
                }
            }
        });

        String nameOfChStr = i.getStringExtra("name");
        String addressStr = i.getStringExtra("city");
        String description = i.getStringExtra("description");
        String score = i.getStringExtra("score");
        String contactInfoStr = i.getStringExtra("contactInfo");
        String key = i.getStringExtra("key");

        nameOfCh.setText(nameOfChStr);
        address.setText(addressStr);
        price.setText(description);
        date.setText(score);
        contactInfo.setText(contactInfoStr);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoTeamsActivity.this, TeamsActivity.class);
                intent.putExtra("key", key);
                startActivity(intent);
                finishAndRemoveTask();
            }
        });
    }
}