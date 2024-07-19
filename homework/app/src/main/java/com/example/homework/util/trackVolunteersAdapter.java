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
// adapter that use to preset the trackVolunteers
public class trackVolunteersAdapter extends ArrayAdapter<trackvolunteer> {

    private Context mContext;
    private int mResource = R.layout.listoftrackvolnteer;

    public trackVolunteersAdapter(Context context, int resource, List<trackvolunteer> trackVolunteersList) {
        super(context, resource, trackVolunteersList);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the trackVolunteers object for this position
        trackvolunteer volunteer = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewOrgName = convertView.findViewById(R.id.organization_name);
        TextView textViewVolunteerName = convertView.findViewById(R.id.volunteer_name);
        TextView textViewDate = convertView.findViewById(R.id.volunteer_date);
        TextView textViewTime = convertView.findViewById(R.id.volunteer_time);
        TextView textViewTaskCompleted = convertView.findViewById(R.id.task_completed);

        // Set the trackVolunteers information to the TextViews
        textViewOrgName.setText(volunteer.getNameOfOrg());
        textViewVolunteerName.setText(volunteer.getName());
        textViewDate.setText(volunteer.getDate());
        textViewTime.setText(volunteer.getTime());
        textViewTaskCompleted.setText(volunteer.getTaskCompleted());

        // Return the completed view to render on the screen
        return convertView;
    }
}
