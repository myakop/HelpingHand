package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
// list of view organizations details and description
public class ViewOrganization extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vieworganization);
        OrganizationDatabase db2 = new OrganizationDatabase(this);
        Cursor cursor = db2.getAllOrganization();
        ImageView home  = findViewById(R.id.homeButton);   // get the data from the database
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewOrganization.this, enterpage.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ViewOrganization.this, homepage.class);
                startActivity(intent);

            }
        });

//  Convert the Cursor to a list of Organization objects
        List<Organization> OrganizationList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String des = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                Organization Organization = new Organization(name, des);
                OrganizationList.add(Organization);
            } while (cursor.moveToNext());
        }

//  Create an instance of your custom ArrayAdapter
        OrganizationAdapter adapter2 = new OrganizationAdapter(this, android.R.layout.simple_list_item_1, OrganizationList);

// Set the ArrayAdapter to the ListView
        ListView listViewDonors = findViewById(R.id.listViewOrganizaton);
        listViewDonors.setAdapter(adapter2);

    }
}
