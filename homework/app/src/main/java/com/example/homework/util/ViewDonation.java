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
import com.example.homework.util.CustomDonorAdapter;
import com.example.homework.util.Donor;
import com.example.homework.util.financialDatabase;

import java.util.ArrayList;
import java.util.List;
//page that present the donations in list for the admin
public class ViewDonation extends Fragment {

    private int selectedPosition = -1;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.donationmanagement, container, false);

        financialDatabase db2 = new financialDatabase(requireActivity());
        db2.getAllDonors3();
        Cursor cursor = db2.getAllDonors();

        List<Donor> donorList = new ArrayList<>();   // get the data from the database
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double donation = cursor.getDouble(cursor.getColumnIndexOrThrow("donation"));
                Donor donor = new Donor(name, donation);
                donorList.add(donor);
            } while (cursor.moveToNext());
        }

        CustomDonorAdapter adapter2 = new CustomDonorAdapter(requireActivity(), android.R.layout.simple_list_item_1, donorList);

        ListView listViewDonors = view.findViewById(R.id.listViewDonors);
        listViewDonors.setAdapter(adapter2);

        ListView listView = view.findViewById(R.id.listViewDonors);
        financialDatabase db = new financialDatabase(requireActivity());
        List<String> itemList = db.getAllDonors2();
        adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        financialDatabase db3 = new financialDatabase(requireActivity(),adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPosition = position;
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View listItem = parent.getChildAt(i);
                    listItem.setBackgroundColor(Color.TRANSPARENT);
                }
                view.setBackgroundColor(Color.LTGRAY);
            }
        });

        Button addButton = view.findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {   // add button
            @Override
            public void onClick(View v) {
                AddPyment(v);
            }
        });
        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() { // edit button
            @Override
            public void onClick(View v) {
                EditPyment(v);
            }
        });
        Button remove = view.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() { // remove button
            @Override
            public void onClick(View v) {
                removeSelectedItem(v);
            }
        });

        return view;
    }

    //remove selected item from the list
    public void removeSelectedItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this record?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() { // confirm button
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
                        // Handle canceling the deletion
                    }
                })
                .setCancelable(false)
                .show();
    }
// remove item from the data base
    private void removeItem(int position) {
        financialDatabase db = new financialDatabase(requireActivity());
        Cursor cursor = db.getAllDonors();
        if (cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            db.DeleteDonation(id,getView());
            cursor.close();
        }
    }

    // go to add page

    public void AddPyment(View v) {
        Intent intent = new Intent(requireActivity(), financialcontribution.class);
        String receivedString = "Admin";
        intent.putExtra("checkAdmin" ,receivedString );
        startActivity(intent);
    }
    // go to edit page
    public void EditPyment(View v) {
        if (selectedPosition != -1) {
            financialDatabase db = new financialDatabase(requireActivity());
            Cursor cursor = db.getAllDonors();
            if (cursor != null && cursor.moveToPosition(selectedPosition)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
               // db.DeleteDonation(id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String donation = cursor.getString(cursor.getColumnIndexOrThrow("donation"));
                cursor.close();
                Intent intent = new Intent(requireActivity(), EditDonation.class);
                intent.putExtra("Name", name);
                intent.putExtra("donation", donation);
                intent.putExtra("id", id);
              //  adapter.remove(adapter.getItem(selectedPosition));
                adapter.notifyDataSetChanged();
                selectedPosition = -1;
                startActivity(intent);
            }
        }
    }
}

