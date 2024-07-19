package com.example.homework.util;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.homework.R;
import com.example.homework.util.CustomDonorAdapter;
import com.example.homework.util.Donor;
import com.example.homework.util.financialDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
// page that present the top donations
public class topvolunteers extends Activity {

    private int selectedPosition = -1;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topvolunteer);

        financialDatabase db2 = new financialDatabase(topvolunteers.this);
        db2.getAllDonors3();
        Cursor cursor = db2.getAllDonors();

        List<Donor> donorList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {  /// get the donationsdata from the database
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                double donation = cursor.getDouble(cursor.getColumnIndexOrThrow("donation"));
                Donor donor = new Donor(name, donation);
                donorList.add(donor);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // Sort the donorList in descending order based on donation
        Collections.sort(donorList, new Comparator<Donor>() {
            @Override
            public int compare(Donor donor1, Donor donor2) {
                return Double.compare(donor2.getDonation(), donor1.getDonation());
            }
        });

        List<Donor> top5Donors = new ArrayList<>();
        int count = Math.min(donorList.size(), 5);
        for (int i = 0; i < count; i++) {
            top5Donors.add(donorList.get(i));
        }

        CustomDonorAdapter adapter2 = new CustomDonorAdapter(topvolunteers.this, android.R.layout.simple_list_item_1, top5Donors);

        ListView listViewDonors = findViewById(R.id.listViewDonors);
        listViewDonors.setAdapter(adapter2);
        listViewDonors.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    }
}
