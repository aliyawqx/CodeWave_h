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

public class ListAdapter extends ArrayAdapter<Studio> {

    Context context;
    ArrayList<Studio> studioArrayList;
    public ListAdapter(@NonNull Context context, ArrayList<Studio> dataArrayList) {
        super(context, R.layout.list_item, dataArrayList);
        this.context = context;
        this.studioArrayList = dataArrayList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        Studio studioData = this.studioArrayList.get(position);
        LayoutInflater inflater = LayoutInflater.from(this.context);
        view = inflater.inflate(R.layout.list_item, null, false);

        ImageView listImageView = view.findViewById(R.id.listImageView);
        TextView listStudioName = view.findViewById(R.id.listStudioName);
        TextView listPrice = view.findViewById(R.id.listPrice);
        TextView listDirections = view.findViewById(R.id.listDirections);

        if (studioData.getImageUri() != null) {
            listImageView.setImageURI(Uri.parse(studioData.getImageUri()));
        }
        listStudioName.setText(studioData.getNameOfCh());
        listPrice.setText(studioData.getDate());
        listDirections.setText(studioData.getAddress());

        view.setTag(studioData.getKey());

        return view;
    }

}
