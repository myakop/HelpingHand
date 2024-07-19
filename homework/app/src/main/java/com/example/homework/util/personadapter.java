package com.example.homework.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.homework.R;

import java.util.ArrayList;
// adapter that used for present  people
public class personadapter extends ArrayAdapter<person> {
private Context context;
private int mresource;
    public personadapter(@NonNull Context context, int resource, @NonNull ArrayList<person> objects) {
        super(context, resource, objects);
        this.context=context;
        this.mresource=resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
// Get the poor object for this position
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        convertView=layoutInflater.inflate(mresource,parent,false);
        ImageView imageView=convertView.findViewById(R.id.img1);
        TextView txtname=convertView.findViewById(R.id.txtname);
        TextView txtdes=convertView.findViewById((R.id.txtdes));
        imageView.setImageResource(getItem(position).getImage());
        txtname.setText(getItem(position).getName());
        txtdes.setText(getItem(position).getDes());


        // Return the completed view to render on the screen
        return convertView;
    }
}

