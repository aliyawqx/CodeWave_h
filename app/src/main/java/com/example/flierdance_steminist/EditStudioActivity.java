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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;

public class EditStudioActivity extends AppCompatActivity {

    EditText editNameOfCh, editAddress, editPrice, editContactInfo, editDate, editTime, editDirection;
    Button saveButton;
    ImageView imageView;
    String nameOfChStudio, iconStudio, addressStudio, priceStudio, contactInfoStudio, dateStudio, timeStudio, directionStudio;
    FirebaseDatabase db;
    DatabaseReference studios, icons;
    FirebaseStorage storage;
    StorageReference storageReference;
    String key, keyUser, icon;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_studio);
        Intent intent = getIntent();
        key = intent.getStringExtra("key");
        keyUser = intent.getStringExtra("keyUser");
        icon = intent.getStringExtra("icon");

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        studios = db.getReference().child("Studios");

        editNameOfCh = findViewById(R.id.editNameOfCh);
        editAddress = findViewById(R.id.editAddress);
        editPrice = findViewById(R.id.editPrice);
        editContactInfo = findViewById(R.id.editContactInfo);
        editDate = findViewById(R.id.editDate);
        editTime = findViewById(R.id.editTime);
        editDirection = findViewById(R.id.editDirection);
        saveButton = findViewById(R.id.saveButton);
        imageView = findViewById(R.id.imageView);

        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        showData();

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        icons = db.getReference().child("Icons");

        storage = FirebaseStorage.getInstance("gs://flierdance-3f911.appspot.com");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            imageUri = data.getData();
                            imageView.setImageURI(imageUri);
                        } else {
                            Toast.makeText(EditStudioActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent imagePicker = new Intent();
//                imagePicker.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                imagePicker.setType("image/*");
//                activityResultLauncher.launch(imagePicker);
//
//            }
//        });
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(EditStudioActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editDate.setText(year + "." + month + "." + day);
                    }
                }, currentYear, currentMonth, currentDay);
                datePickerDialog.show();
            }
        });

        editTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditStudioActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        editTime.setText(hourOfDay + ":" + minutes);
                    }
                }, currentHour, currentMinute, true);
                timePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean nameOfCh = isNameOfChChanged();
                boolean address = isAddressChanged();
                boolean price = isPriceChanged();
                boolean contactInfo = isContactInfoChanged();
                boolean date = isDateChanged();
                boolean time = isTimeChanged();
                boolean direction = isDirectionChanged();
                boolean image = isIconChanged();
                if (priceStudio.isEmpty() || nameOfChStudio.isEmpty() || addressStudio.isEmpty() || contactInfoStudio.isEmpty() || directionStudio.isEmpty() || timeStudio.isEmpty() || dateStudio.isEmpty()) {
                    Toast.makeText(EditStudioActivity.this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                } else if (nameOfCh || image || address || price || contactInfo || date || time || direction) {
                    uploadToFirebase(imageUri);
                } else {
                    Toast.makeText(EditStudioActivity.this, "No Changes Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void uploadToFirebase(Uri uri){
        if (uri != null) {
            storageReference = storage.getReference().child(System.currentTimeMillis() + "." + getFileExtension(uri));
            storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Intent intent = new Intent(EditStudioActivity.this, MyStudioActivity.class);
                            intent.putExtra("key", key);
                            intent.putExtra("keyUser", keyUser);
                            finishAndRemoveTask();
                            Toast.makeText(EditStudioActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditStudioActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            Intent intent = new Intent(EditStudioActivity.this, MyStudioActivity.class);
            intent.putExtra("key", key);
            intent.putExtra("keyUser", keyUser);
            finishAndRemoveTask();
            Toast.makeText(EditStudioActivity.this, "Saved", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
    }
    private String getFileExtension(Uri fileUri){
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }

    public boolean isIconChanged(){
        if (iconStudio != null){
            studios.child(key).child("imageUri").setValue(imageUri.toString());
            imageView.setImageURI(imageUri);
            return true;
        } else{
            return false;
        }
    }

    public boolean isPriceChanged(){
        if (!priceStudio.equals(editPrice.getText().toString())){
            studios.child(key).child("price").setValue(editPrice.getText().toString());
            priceStudio = editPrice.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isAddressChanged(){
        if (!addressStudio.equals(editAddress.getText().toString())){
            studios.child(key).child("address").setValue(editAddress.getText().toString());
            addressStudio = editAddress.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isNameOfChChanged(){
        if (!nameOfChStudio.equals(editNameOfCh.getText().toString())){
            studios.child(key).child("nameOfCh").setValue(editNameOfCh.getText().toString());
            nameOfChStudio = editNameOfCh.getText().toString();
            return true;
        } else{
            return false;
        }
    }


    public boolean isContactInfoChanged(){
        if (!contactInfoStudio.equals(editContactInfo.getText().toString())){
            studios.child(key).child("contactInfo").setValue(editContactInfo.getText().toString());
            contactInfoStudio = editContactInfo.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isDateChanged(){
        if (!dateStudio.equals(editDate.getText().toString())){
            studios.child(key).child("date").setValue(editDate.getText().toString());
            dateStudio = editDate.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isTimeChanged(){
        if (!timeStudio.equals(editTime.getText().toString())){
            studios.child(key).child("time").setValue(editTime.getText().toString());
            timeStudio = editTime.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public boolean isDirectionChanged(){
        if (!directionStudio.equals(editDirection.getText().toString())){
            studios.child(key).child("direction").setValue(editDirection.getText().toString());
            directionStudio = editDirection.getText().toString();
            return true;
        } else{
            return false;
        }
    }

    public void showData(){
        Auth.getStudioByKey(key, new StudioDataListener() {
            @Override
            public void OnStudioDataReady(Studio studio) {
                nameOfChStudio = studio.getNameOfCh();
                addressStudio = studio.getAddress();
                priceStudio = studio.getPrice();
                contactInfoStudio = studio.getContactInfo();
                directionStudio = studio.getDirection();
                timeStudio = studio.getTime();
                dateStudio = studio.getDate();
                iconStudio = studio.getImageUri();

                editNameOfCh.setText(nameOfChStudio);
                editAddress.setText(addressStudio);
                editPrice.setText(priceStudio);
                editContactInfo.setText(contactInfoStudio);
                editDirection.setText(directionStudio);
                editDate.setText(dateStudio);
                editTime.setText(timeStudio);
                if (iconStudio != null){
                    imageView.setImageURI(Uri.parse(iconStudio));
                }
            }
        });

    }
}