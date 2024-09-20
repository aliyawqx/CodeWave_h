package com.example.flierdance_steminist;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.flierdance_steminist.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class NewStudioActivity extends AppCompatActivity {

    EditText profileAddress, profilePrice, profileTime, profileDate, titleNameCh, profileContactInfo, profileDirection;
    String addressStr, priceStr, nameOfChStr, contactInfoStr, directionStr, timeStr, dateStr, userStr;
    Button doneButton;
    FirebaseDatabase db;
    DatabaseReference stores;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_studio);
        profileAddress = findViewById(R.id.profileAddress);
        profilePrice = findViewById(R.id.profilePrice);
        titleNameCh = findViewById(R.id.titleNameOfCh);
        profileContactInfo = findViewById(R.id.profileContactInfo);
        profileDirection = findViewById(R.id.profileDirection);
        doneButton = findViewById(R.id.doneButton);
        profileDate = findViewById(R.id.profileDate);
        profileTime = findViewById(R.id.profileTime);

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        stores = db.getReference().child("Studios");

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        profileTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(NewStudioActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        profileTime.setText(hourOfDay + ":" + minutes);
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });
        profileDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewStudioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        profileDate.setText(year + "." + month + "." + day);
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addressStr = profileAddress.getText().toString();
                nameOfChStr = titleNameCh.getText().toString();
                priceStr = profilePrice.getText().toString();
                contactInfoStr = profileContactInfo.getText().toString();
                directionStr = profileDirection.getText().toString();
                timeStr = profileTime.getText().toString();
                dateStr = profileDate.getText().toString();
                if (nameOfChStr.isEmpty() || addressStr.isEmpty() || timeStr.isEmpty() || dateStr.isEmpty() || priceStr.isEmpty() || contactInfoStr.isEmpty()){
                    Toast.makeText(NewStudioActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Studio studio = new Studio(nameOfChStr, addressStr, priceStr, dateStr, timeStr, contactInfoStr, directionStr,userStr);
                    DatabaseReference push = stores.push();
                    String key = push.getKey();
                    studio.setKey(key);
                    push.setValue(studio);
                    System.out.println(studio.getImageUri());
                    Intent intent = new Intent(NewStudioActivity.this, MainActivity.class);
                    intent.putExtra("key", userStr);
                    Toast.makeText(NewStudioActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
            }
        });
    }
}