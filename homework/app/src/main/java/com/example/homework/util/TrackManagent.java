package com.example.homework.util;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.R;

import java.util.ArrayList;
import java.util.List;
// page that show the volunteers that mathed to the orgnazation to track them
public class TrackManagent extends Fragment {
    private int selectedPosition = -1;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.trackvolunteers, container, false);
        //  Retrieve data from the database
        trackVolunteersDataBase db2 = new trackVolunteersDataBase(getActivity());


        db2.getAllVolunteers3();
        Cursor cursor = db2.getAllVolunteers();

        //  Convert the Cursor to a list of Donor objects
        List<trackvolunteer> VolunteerList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String org = cursor.getString(cursor.getColumnIndexOrThrow("nameoforg"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String Date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String Time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String taskcomplete = cursor.getString(cursor.getColumnIndexOrThrow("taskcompleted"));
                trackvolunteer Volunteer = new trackvolunteer(org,name, Date, Time, taskcomplete);
                VolunteerList.add(Volunteer);
            } while (cursor.moveToNext());
        }

        //  Create an instance of your custom ArrayAdapter
        trackVolunteersAdapter adapter2 = new trackVolunteersAdapter(getActivity(), android.R.layout.simple_list_item_1, VolunteerList);

        // Set the ArrayAdapter to the ListView
        ListView listViewDonors = rootView.findViewById(R.id.listTrackVoluneer);
        listViewDonors.setAdapter(adapter2);

        ListView listView = rootView.findViewById(R.id.listTrackVoluneer);
        trackVolunteersDataBase db = new trackVolunteersDataBase(getActivity());
        List<String> itemList = db.getAllVolunteers2();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        trackVolunteersDataBase db3 = new trackVolunteersDataBase(getActivity() , adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                // Reset the background color for all items
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.TRANSPARENT);
                }
                // Set the background color for the selected item
                view.setBackgroundColor(Color.LTGRAY);
                // Update the list view after item removal
            }
        });

        return rootView;
    }
}
