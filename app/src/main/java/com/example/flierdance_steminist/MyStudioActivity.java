package com.example.flierdance_steminist;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyStudioActivity extends AppCompatActivity {
    ListView myList;
    ListAdapter adapter;
    FirebaseDatabase db;
    DatabaseReference studios;
    ArrayList<Studio> studioArrayList = new ArrayList<>();
    Auth auth = new Auth(MyStudioActivity.this);
    private static final int INFOSTUDIOACTIVITY = 1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_studio);

        Intent intent = getIntent();
        String key = intent.getStringExtra("keyUser");

        myList = findViewById(R.id.listView);
        adapter = new ListAdapter(this, studioArrayList);
        myList.setAdapter(adapter);

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        studios = db.getReference().child("Studios");

        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) view.getTag();
                for (Studio s : studioArrayList) {
                    if (s.getKey().equals(id)) {
                        Intent intent = new Intent(MyStudioActivity.this,
                                EditStudioActivity.class);
                        intent.putExtra("key", id);
                        intent.putExtra("keyUser", key);
                        startActivityForResult(intent, INFOSTUDIOACTIVITY);
                        finishAndRemoveTask();
                        break;
                    }
                }
            }
        });

        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MyStudioActivity.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                studios.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot document : snapshot.getChildren()) {
                                            Studio studio = document.getValue(Studio.class);
                                            studios.child(studio.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        adapter.notifyDataSetChanged();
                                                        Toast.makeText(MyStudioActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                                        MyProfileFragment myProfileFragment = new MyProfileFragment();
                                                        getSupportFragmentManager().beginTransaction()
                                                                .replace(R.id.content_frame2, myProfileFragment)
                                                                .addToBackStack(MyStudioActivity.class.getSimpleName())
                                                                .commit();
                                                    }
                                                    else{
                                                        Toast.makeText(MyStudioActivity.this, "Something went wrong. Please try again", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        adapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }).setNegativeButton("No", null);
                builder.show();
                return true;
            }
        });
        studios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio studio = document.getValue(Studio.class);
                    if (studio.getUser().equals(key)){
                        studioArrayList.add(studio);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}