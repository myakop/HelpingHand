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
// adapter that used to preset Organization
public class OrganizationAdapter extends ArrayAdapter<Organization> {

    private Context mContext;
    private int mResource = R.layout.listoforganization;

    public OrganizationAdapter(Context context, int resource, List<Organization> Organization) {
        super(context, resource, Organization);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the organization object for this position
        Organization Organization = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.nameofOrganization);
        TextView textViewDate = convertView.findViewById(R.id.desOrganization);

        // Set the organization information to the TextViews
        textViewName.setText(Organization.getName());
        textViewDate.setText(Organization.getDes());

        // Return the completed view to render on the screen
        return convertView;
    }

}
