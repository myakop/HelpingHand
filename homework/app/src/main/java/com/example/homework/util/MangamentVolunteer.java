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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.homework.R;

import java.util.ArrayList;
import java.util.List;
// Fragment of showing Volunteer Helps and hours with add remove and edit buttons
public class MangamentVolunteer extends Fragment {

    private int selectedPosition = -1;
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.managementvolunteer, container, false);
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
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Addvolunteer2(v);
            }  // add button
        });
        Button edit = rootView.findViewById(R.id.editbutton);
        edit.setOnClickListener(new View.OnClickListener() {  // edit button
            @Override
            public void onClick(View v) {
                Editvounter(v);
            }
        });
        Button remove = rootView.findViewById(R.id.removebutton); // remove button
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeSelectedItem(v);
            }
        });

        return rootView;
    }

    // remove the item that selected
    public void removeSelectedItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this record?")     // alert to confirm
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedPosition != -1) {
                            removeItem(selectedPosition);
                            adapter.remove(adapter.getItem(selectedPosition));
                            adapter.notifyDataSetChanged();
                            selectedPosition = -1;



                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setCancelable(false)
                .show();
    }

    // remove the item from the list
    private void removeItem(int position) {
        VolunteerDataBase db = new VolunteerDataBase(getActivity());
        Cursor cursor = db.getAllVolunteers();
        if (cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            db.DeleteVolunteer(id,getView());
            cursor.close();
        }
    }
    // button go to add new help of volunteer
    public void Addvolunteer2(View v) {
        Intent intent = new Intent(getActivity(), physicaldonation.class);
        intent.putExtra("checkAdmin", "Admin");
        startActivity(intent);
    }

    // button go to edit  help of volunteer
    public void Editvounter(View v) {
        if (selectedPosition != -1) {
            VolunteerDataBase db = new VolunteerDataBase(getActivity());
            Cursor cursor = db.getAllVolunteers();
            if (cursor != null && cursor.moveToPosition(selectedPosition)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
             //   db.DeleteVolunteer(id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String Time = cursor.getString(cursor.getColumnIndexOrThrow("time"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("Description"));
                cursor.close();
                Intent intent = new Intent(getActivity(), EditVolunteer.class);
                intent.putExtra("Name", name);
                intent.putExtra("des", des);
                intent.putExtra("id",id);
                intent.putExtra("time",Time);

             //   adapter.remove(adapter.getItem(selectedPosition));
             //   removeItem(selectedPosition);
                adapter.notifyDataSetChanged();
                selectedPosition = -1;
                startActivity(intent);
            }
        }
    }
}
