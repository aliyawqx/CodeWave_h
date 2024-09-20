package com.example.flierdance_steminist;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class SetAvatarActivity extends AppCompatActivity {
    ImageView imageView;
    FirebaseStorage storage;
    Uri imageUri;
    FirebaseDatabase db;
    DatabaseReference icons;
    StorageReference storageReference;
    ProgressBar progressBar;
    FloatingActionButton uploadButton;
    Intent intent = new Intent();
    String keyUser = intent.getStringExtra("a");
    String key = intent.getStringExtra("key");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_set_avatar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("New avatar");

        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        uploadButton = findViewById(R.id.uploadButton);
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
                            uploadToFirebase(imageUri);
                        } else {
                            Toast.makeText(SetAvatarActivity.this, "No image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoPicker = new Intent();
                videoPicker.setAction(Intent.ACTION_GET_CONTENT);
                videoPicker.setType("image/*");
                activityResultLauncher.launch(videoPicker);

            }
        });
    }

    private void uploadToFirebase(Uri uri){
        storageReference = storage.getReference().child(System.currentTimeMillis() + "." + getFileExtension(uri));
        storageReference.putFile(uri).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SetAvatarActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SetAvatarActivity.this, EditStudioActivity.class);
                        intent.putExtra("keyUser", keyUser);
                        intent.putExtra("icon", uri.toString());
                        intent.putExtra("key", key);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SetAvatarActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}