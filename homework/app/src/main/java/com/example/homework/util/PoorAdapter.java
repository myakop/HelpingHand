package com.example.homework.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.homework.R;

import java.util.List;
// adapter for present poor people
public class PoorAdapter extends ArrayAdapter<PoorPeople> {
    private Context mContext;
    private int mResource = R.layout.listofpoorpeople;

    public PoorAdapter(Context context, int resource, List<PoorPeople> PoorPeople) {
        super(context, resource, PoorPeople);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the Poor object for this position
        PoorPeople PoorPeople = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.nameOfPoor);
        TextView textViewDate = convertView.findViewById(R.id.Addressofpoor);
        TextView textViewTime = convertView.findViewById(R.id.phonePoor);


        // Set the Poor information to the TextViews
        textViewName.setText(PoorPeople.getName());
        textViewDate.setText(PoorPeople.getAddress());
        textViewTime.setText(PoorPeople.getPhone());


        // Return the completed view to render on the screen
        return convertView;
    }
}
