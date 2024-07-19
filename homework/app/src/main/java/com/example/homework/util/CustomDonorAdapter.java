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
// adapter that used to present donations in listview
public class CustomDonorAdapter extends ArrayAdapter<Donor> {
    private Context mContext;
    private int mResource = R.layout.listofdonation;

    public CustomDonorAdapter(Context context, int resource, List<Donor> donors) {
        super(context, resource, donors);
        mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the donor object for this position
        Donor donor = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        TextView textViewName = convertView.findViewById(R.id.textViewDonorName);
        TextView textViewDonation = convertView.findViewById(R.id.textViewDonation);

        // Set the donor information to the TextViews
        textViewName.setText(donor.getName());
        textViewDonation.setText(String.valueOf(donor.getDonation()));

        // Return the completed view to render on the screen
        return convertView;
    }
}

