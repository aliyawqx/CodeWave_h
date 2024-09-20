package com.example.flierdance_steminist;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


public class InfoActivity extends AppCompatActivity {
    TextView nameOfCh, address, price, time, date, contactInfo;
    Auth auth;
    Button buyBtn;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
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

        String nameOfChStr = i.getStringExtra("nameOfCh");
        String addressStr = i.getStringExtra("address");
        String priceStr = i.getStringExtra("direction");
        String dateStr = i.getStringExtra("date");
        String timeStr = i.getStringExtra("time");
        String contactInfoStr = i.getStringExtra("contactInfo");

        nameOfCh.setText(nameOfChStr);
        address.setText(addressStr);
        price.setText(priceStr);
        date.setText(dateStr);
        time.setText(timeStr);
        contactInfo.setText(contactInfoStr);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(InfoActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.trial_lesson, null);
                EditText info = dialogView.findViewById(R.id.infoBox);
                info.setText(nameOfChStr + " will wait you " + dateStr + " at " + timeStr + " in " + addressStr +".");
                builder.setView(dialogView);
                AlertDialog dialog = builder.create();
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
    }
}