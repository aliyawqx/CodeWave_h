package com.example.flierdance_steminist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddVideoActivity extends AppCompatActivity {

    FloatingActionButton uploadButton;
    VideoView uploadVideo;
    EditText uploadCaption;
    ProgressBar progressBar;
    Uri videoUri;
    FirebaseStorage storage;
    FirebaseDatabase db;
    DatabaseReference videos;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_video);
        uploadButton = findViewById(R.id.uploadButton);
        uploadCaption = findViewById(R.id.uploadCaption);
        uploadVideo = findViewById(R.id.uploadVideo);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        videos = db.getReference().child("Videos");

        storage = FirebaseStorage.getInstance("gs://flierdance-3f911.appspot.com");

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            videoUri = data.getData();
                            uploadVideo.setVideoURI(videoUri);
                        } else {
                            Toast.makeText(AddVideoActivity.this, "No video Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent videoPicker = new Intent();
                videoPicker.setAction(Intent.ACTION_GET_CONTENT);
                videoPicker.setType("image/*");
                activityResultLauncher.launch(videoPicker);
            }
        });
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoUri != null){
                    uploadToFirebase(videoUri);
                } else  {
                    Toast.makeText(AddVideoActivity.this, "Please select video", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void uploadToFirebase(Uri uri){
        Intent intent = new Intent();
        String keyUser = intent.getStringExtra("keyUser");
        String caption = uploadCaption.getText().toString();
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
                        DatabaseReference push = videos.push();
                        String key = push.getKey();
                        Video videoClass = new Video(uri.toString(), caption, keyUser);
                        videos.child(key).setValue(videoClass);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddVideoActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddVideoActivity.this, MainActivity.class);
                        intent.putExtra("key", keyUser);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AddVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}