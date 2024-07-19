package com.example.homework.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.R;

import java.util.ArrayList;
import java.util.List;
/// match a volunteer from the list to the orgnazation
public class match extends Fragment {
    private int selectedPosition = -1;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.viewvolunteersformatch, container, false);
        //  Retrieve data from the database
        VolunteerDataBase db2 = new VolunteerDataBase(getActivity());
        db2.getAllVolunteers3();
        Cursor cursor = db2.getAllVolunteers();

        //  Convert the Cursor to a list of Donor objects
        List<Volunteer> VolunteerList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String Date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String Time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                Volunteer Volunteer = new Volunteer(name, Date, Time, des);
                VolunteerList.add(Volunteer);
            } while (cursor.moveToNext());
        }

        //  Create an instance of your custom ArrayAdapter
        VolunteerAdapter adapter2 = new VolunteerAdapter(getActivity(), android.R.layout.simple_list_item_1, VolunteerList);

        // Set the ArrayAdapter to the ListView
        ListView listViewDonors = rootView.findViewById(R.id.listViewVoluneer);
        listViewDonors.setAdapter(adapter2);

        ListView listView = rootView.findViewById(R.id.listViewVoluneer);
        VolunteerDataBase db = new VolunteerDataBase(getActivity());
        List<String> itemList = db.getAllVolunteers2();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        VolunteerDataBase db3 = new VolunteerDataBase(getActivity(),adapter);
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
        Button addButton = rootView.findViewById(R.id.button8);
        trackVolunteersDataBase data = new trackVolunteersDataBase(getActivity());
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selectedPosition >= 0 && selectedPosition < VolunteerList.size()) {
                    Volunteer selectedVolunteer = VolunteerList.get(selectedPosition);
                    Bundle args = getArguments();

                        // Retrieve the data from the Bundle using the key "KEY_DATA"
                    String receivedData = args.getString("KEY_DATA");

                    // Extract data from the selected Volunteer object
                    String nameOfOrg = receivedData;
                    String name = selectedVolunteer.getName();
                    String date = selectedVolunteer.getDate();
                    String time = selectedVolunteer.getTime();
                    String taskCompleted = "No"; // Set this to "Yes" if the task is completed, otherwise "No"

                    // Create a new trackVolunteers object
                    data.addVolunteer(nameOfOrg, name, date, time, taskCompleted);






                    // Show a dialog or a toast message to indicate that the volunteer has been added
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Volunteer Added")
                            .setMessage("Volunteer added to trackVolunteers: " + name)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Handle the OK button click if needed
                                }
                            })
                            .show();
                } else {
                    // If no item is selected, show an error message or handle it as appropriate for your app.
                    // For example, you can display a toast message like this:
                    Toast.makeText(getActivity(), "Please select a volunteer from the list.", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return rootView;
    }
}
