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

// class for edit donation
public class EditDonation extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // timeButton1 = findViewById(R.id.timeButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdonaton);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Name");   // take the data of the old donation that i want to update
        String message2 = intent.getStringExtra("donation");
        EditText name = findViewById(R.id.nameOfVolunter);
        EditText des = findViewById(R.id.donationAmount);
        name.setText(message);
        des.setText(message2);

        ImageView home  = findViewById(R.id.homeButton); // set action for the tool bar
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() { // go to home page
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditDonation.this, admin.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() { // log out and go to login page
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(EditDonation.this, homepage.class);
                startActivity(intent);

            }
        });
    }


    // submit edit the donation
    public void SumbitDonate(View v) {
        financialDatabase data =  new financialDatabase(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Edit")                                        // set confirm alert
                .setMessage("Are you sure you want to Edit this record?")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  // confirm button
                        Intent intent2 = getIntent();
                        int id = intent2.getIntExtra("id",0);
                        data.DeleteDonation(id,v);
                        EditText name = findViewById(R.id.nameOfVolunter) ;
                        EditText donation = findViewById(R.id.donationAmount) ;
                        String nameofthevoluner = name.getText().toString();
                        String donation2 = donation.getText().toString();
                        double amount = Double.parseDouble(donation2);;
                        data.addDonor(nameofthevoluner ,amount, v,2 );
                        editfFragment dialogFragment = new editfFragment();
                        dialogFragment.show(getSupportFragmentManager(), "example_dialog");

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  // cancel button
                        Intent intent = getIntent();
                        String message = intent.getStringExtra("Name");
                        String message2 = intent.getStringExtra("dantion");
                        EditText name = findViewById(R.id.nameOfVolunter);
                        EditText des = findViewById(R.id.donationAmount);
                        name.setText(message);
                        des.setText(message2);
                        Intent intent2 = new Intent(EditDonation.this, admin.class);
                        startActivity(intent2);
                    }
                })
                .setCancelable(false)
                .show();

    }



}
