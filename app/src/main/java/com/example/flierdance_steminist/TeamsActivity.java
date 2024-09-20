package com.example.flierdance_steminist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TeamsActivity extends AppCompatActivity {
    ListView myList, myListSearch, myListSprinner, myListSprinnerPrice;
    Spinner spinner, spinnerPrice;
    Adapter adapter;
    FirebaseDatabase db;
    DatabaseReference teams;
    List<String> city, score;
    ArrayList<Team> teamArrayList = new ArrayList<>();
    String key;
    private static final int TEAMSACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teams);

        SearchView searchView = findViewById(R.id.search);
        searchView.clearFocus();
        Intent i = new Intent();
        key = i.getStringExtra("key");

        myList = findViewById(R.id.listView);
        adapter = new Adapter(getApplicationContext(), teamArrayList);
        myList.setAdapter(adapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Team> studioAL = new ArrayList<>();
                for (Team studio : teamArrayList){
                    if (studio.getName().toLowerCase().contains(s.toLowerCase())){
                        studioAL.add(studio);
                        myListSearch = findViewById(R.id.listView);
                        adapter = new com.example.flierdance_steminist.Adapter(getApplicationContext(), studioAL);
                        myListSearch.setAdapter(adapter);
                    }
                } if (studioAL.isEmpty()){
                    myListSearch = findViewById(R.id.listView);
                    adapter = new com.example.flierdance_steminist.Adapter(getApplicationContext(), studioAL);
                    myListSearch.setAdapter(adapter);
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        teams = db.getReference().child("Teams");

        teams.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Team team = document.getValue(Team.class);
                    teamArrayList.add(team);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        afterCreate();
    }
    private void afterCreate() {
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) view.getTag();
                for (Team s : teamArrayList) {
                    if (s.getKey().equals(id)) {
                        Intent intent = new Intent(getApplicationContext(),
                                InfoTeamsActivity.class);
                        intent.putExtra("key", key);
                        intent.putExtra("name", s.getName());
                        intent.putExtra("city", s.getCity());
                        intent.putExtra("description", s.getDescription());
                        intent.putExtra("contactInfo", s.getContactInfo());
                        intent.putExtra("score", String.valueOf(s.getScore()));
                        startActivityForResult(intent, TEAMSACTIVITY);
                        break;
                    }
                }
            }
        });
    }
}