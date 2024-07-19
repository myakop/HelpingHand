package com.example.homework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

// class for edit Orgnization
public class EditOrgnization extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState) {
        // timeButton1 = findViewById(R.id.timeButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editorgnaization);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Name"); // take the data of the old Orgnization that i want to update
        String message2 = intent.getStringExtra("des");
        EditText name = findViewById(R.id.nameOforg);
        EditText des = findViewById(R.id.descrptionOrg);
        name.setText(message);
        des.setText(message2);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {    // set action for the tool bar
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditOrgnization.this, admin.class); // go to home page
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(EditOrgnization.this, homepage.class); // logout and go to login page
                startActivity(intent);

            }
        });
    }
    // submit edit the Organization
    public void SumbitAdd2(View v) {
        OrganizationDatabase data = new OrganizationDatabase(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Edit")                                         // set confirm alert
                .setMessage("Are you sure you want to Edit this record?")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  // confirm button
                        Intent intent2 = getIntent();
                        int id = intent2.getIntExtra("id",0);
                        data.DeleteOrganization(id,v);
                        EditText name = findViewById(R.id.nameOforg) ;
                        EditText des = findViewById(R.id.descrptionOrg) ;
                        String name2 = name.getText().toString();
                        String des2 = des.getText().toString();
                        data.insertOrganization(name2,des2,v,2);
                        editfFragment dialogFragment = new editfFragment();
                        dialogFragment.show(getSupportFragmentManager(), "example_dialog");



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //  canceling button
                        Intent intent = getIntent();
                        String message = intent.getStringExtra("Name");
                        String message2 = intent.getStringExtra("des");
                        EditText name = findViewById(R.id.nameOforg);
                        EditText des = findViewById(R.id.descrptionOrg);
                        name.setText(message);
                        des.setText(message2);
                        Intent intent2 = new Intent(EditOrgnization.this, admin.class);
                        startActivity(intent2);

                    }
                })
                .setCancelable(false)
                .show();

    }
}
