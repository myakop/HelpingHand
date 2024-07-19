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
// adapter that used to present volunteers hours and date that they can help in them
public class VolunteerAdapter extends ArrayAdapter<Volunteer> {

    private Context mContext;
    private int mResource = R.layout.listofvolunteer;

    public VolunteerAdapter(Context context, int resource, List<Volunteer> volunteers) {
        super(context, resource, volunteers);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the VolunteerHelp object for this position
        Volunteer volunteer = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.nameOfVolunter);
        TextView textViewDate = convertView.findViewById(R.id.dateofvolunteer);
        TextView textViewTime = convertView.findViewById(R.id.timeofvolunteer);
        TextView textViewdes = convertView.findViewById(R.id.descriptionofvolunteer);


        // Set the VolunteerHelp information to the TextViews
        textViewName.setText(volunteer.getName());
        textViewDate.setText(volunteer.getDate());
        textViewTime.setText(volunteer.getTime());
        textViewdes.setText(volunteer.getDes());


        // Return the completed view to render on the screen
        return convertView;
    }
}
