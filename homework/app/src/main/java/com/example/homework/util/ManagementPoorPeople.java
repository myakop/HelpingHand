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
import com.example.homework.util.PoorAdapter;
import com.example.homework.util.PoorPeople;
import com.example.homework.util.PoorPeopleDatabase;

import java.util.ArrayList;
import java.util.List;
// Fragment of showing Poor people with add remove and edit buttons
public class ManagementPoorPeople extends Fragment {

    private int selectedPosition = -1;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.managementpoorpeople, container, false);

        PoorPeopleDatabase db2 = new PoorPeopleDatabase(requireActivity());
        db2.getAllPoorPeople3();
        Cursor cursor = db2.getAllPoorPeople();
        List<PoorPeople> PoorpeopleList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {                    //full in all the list
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                PoorPeople poor = new PoorPeople(name, address, phone);
                PoorpeopleList.add(poor);
            } while (cursor.moveToNext());
        }

        PoorAdapter adapter2 = new PoorAdapter(requireActivity(), android.R.layout.simple_list_item_1, PoorpeopleList);

        ListView listViewDonors = view.findViewById(R.id.listofpoor);
        listViewDonors.setAdapter(adapter2);


        ListView listView = view.findViewById(R.id.listofpoor);
        PoorPeopleDatabase db = new PoorPeopleDatabase(requireActivity());
        List<String> itemList = db.getAllPoorPeople2();
        adapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_list_item_1, itemList);
        listView.setAdapter(adapter);
        PoorPeopleDatabase db3 = new PoorPeopleDatabase(requireActivity() ,adapter);
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
        addButton.setOnClickListener(new View.OnClickListener() { // add button
            @Override
            public void onClick(View v) {
                AddPoor(v);
            }
        });
        Button edit = view.findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {  // edit button
            @Override
            public void onClick(View v) {
                EditPoor(v);
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
    // remove selected item
    public void removeSelectedItem(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this record?")  //confirm delete
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
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  // cancel button
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false)
                .show();
    }
    // remove item from the list
    private void removeItem(int position) {
        PoorPeopleDatabase db = new PoorPeopleDatabase(requireActivity());
        Cursor cursor = db.getAllPoorPeople();
        if (cursor != null && cursor.moveToPosition(position)) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            db.deletePoorPerson(id,getView());
            cursor.close();
        }
    }
    // button go to add  new  poor people
    public void AddPoor(View v) {
        Intent intent = new Intent(requireActivity(), AddPoorControll.class);
        startActivity(intent);
    }
    // button go to edit  poor people
    public void EditPoor(View v) {
        if (selectedPosition != -1) {
            PoorPeopleDatabase db = new PoorPeopleDatabase(requireActivity());
            Cursor cursor = db.getAllPoorPeople();
            if (cursor != null && cursor.moveToPosition(selectedPosition)) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                //db.deletePoorPerson(id);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
                cursor.close();
                Intent intent = new Intent(requireActivity(), EditPoorPeople.class);
                intent.putExtra("Name", name);
                intent.putExtra("address", address);
                intent.putExtra("phone", phone);
                intent.putExtra("id" , id);
               // adapter.remove(adapter.getItem(selectedPosition));
                adapter.notifyDataSetChanged();
                selectedPosition = -1;
                startActivity(intent);
            }
        }
    }
}

