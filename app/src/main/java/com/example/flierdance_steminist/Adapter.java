package com.example.flierdance_steminist;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Adapter extends ArrayAdapter<Team> {
    Context context;
    ArrayList<Team> teamArrayList;
    public Adapter(@NonNull Context context, ArrayList<Team> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.teamArrayList = dataArrayList;
    }

    public Adapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Team studioData = this.teamArrayList.get(position);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        view = inflater.inflate(R.layout.item_team, null, false);

        TextView listName = view.findViewById(R.id.listName);
        TextView listScore = view.findViewById(R.id.listScore);
        TextView listCity = view.findViewById(R.id.listCity);

        listName.setText(studioData.getName());
        listScore.setText(String.valueOf(studioData.getScore()));
        listCity.setText(studioData.getCity());

        view.setTag(studioData.getKey());

        return view;
    }
}
