package com.example.flierdance_steminist;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class HomeFragment extends Fragment {

    ListView myList, myListSearch, myListSprinner, myListSprinnerPrice;
    Spinner spinner, spinnerPrice;
    ListAdapter adapter;
    FirebaseDatabase db;
    DatabaseReference studios;
    List<Set<String>> list, list2;
    List<String> directions, prices;
    ArrayList<Studio> studioArrayList = new ArrayList<>();
    private static final int INFOSTUDIOACTIVITY = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        spinner = view.findViewById(R.id.spinner);
        spinnerPrice = view.findViewById(R.id.spinnerPrice);

        SearchView searchView = view.findViewById(R.id.search);
        searchView.clearFocus();

        myList = view.findViewById(R.id.listView);
        adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioArrayList);
        myList.setAdapter(adapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ArrayList<Studio> studioAL = new ArrayList<>();
                for (Studio studio : studioArrayList){
                    if (studio.getNameOfCh().toLowerCase().contains(s.toLowerCase())){
                        studioAL.add(studio);
                        myListSearch = view.findViewById(R.id.listView);
                        adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioAL);
                        myListSearch.setAdapter(adapter);
                    }
                } if (studioAL.isEmpty()){
                    myListSearch = view.findViewById(R.id.listView);
                    adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioAL);
                    myListSearch.setAdapter(adapter);
                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });

        prices = new ArrayList<>();

        directions = new ArrayList<>();

        db = FirebaseDatabase.getInstance("https://flierdance-3f911-default-rtdb.europe-west1.firebasedatabase.app/");
        studios = db.getReference().child("Studios");
        studios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot document : snapshot.getChildren()) {
                    Studio studio = document.getValue(Studio.class);
                    studioArrayList.add(studio);
                    String spinnerD = document.child("address").getValue(String.class);
                    directions.add(spinnerD);
                    String spinnerP = document.child("date").getValue(String.class);
                    prices.add(spinnerP);
                    System.out.println(directions);
                }
                ((BaseAdapter)adapter).notifyDataSetChanged();
                afterCreate();

                Set<String> set = new HashSet<>(directions);
                directions.clear();
                directions.add("Choose city");
                directions.addAll(set);

                Set<String> set1 = new HashSet<>(prices);
                prices.clear();
                prices.add("Choose date");
                prices.addAll(set1);

                ArrayAdapter<String> adapter1 = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, directions);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter1);

                ArrayAdapter<String> adapter2 = new ArrayAdapter(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, prices);
                adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerPrice.setAdapter(adapter2);

                myListSprinner = view.findViewById(R.id.listView);
                myListSprinnerPrice = view.findViewById(R.id.listView);

                AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        ArrayList<Studio> studioSpinner = new ArrayList<>();
                        String item = (String)adapterView.getItemAtPosition(i);
                        if (item.equals("Choose city")) {
                            adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioArrayList);
                            myList.setAdapter(adapter);
                        }
                        if (item.equals("Choose date")) {
                            adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioArrayList);
                            myList.setAdapter(adapter);
                        }
                        for (Studio studio : studioArrayList){
                            if (studio.getPrice().equals(item)) {
                                studioSpinner.add(studio);
                                adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioSpinner);
                                myListSprinner.setAdapter(adapter);
                            } if (studio.getDate().equals(item)) {
                                studioSpinner.add(studio);
                                adapter = new com.example.flierdance_steminist.ListAdapter(getActivity(), studioSpinner);
                                myListSprinner.setAdapter(adapter);
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                };
                spinner.setOnItemSelectedListener(itemSelectedListener);
                spinnerPrice.setOnItemSelectedListener(itemSelectedListener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
    private void afterCreate() {
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = (String) view.getTag();
                for (Studio s : studioArrayList) {
                    if (s.getKey().equals(id)) {
                        Intent intent = new Intent(getActivity(),
                                InfoActivity.class);
                        intent.putExtra("nameOfCh", s.getNameOfCh());
                        intent.putExtra("address", s.getAddress());
                        intent.putExtra("direction", s.getDirection());
                        intent.putExtra("date", s.getDate());
                        intent.putExtra("time", s.getTime());
                        intent.putExtra("contactInfo", s.getContactInfo());
                        startActivityForResult(intent, INFOSTUDIOACTIVITY);
                        break;
                    }
                }
            }
        });
    }

}