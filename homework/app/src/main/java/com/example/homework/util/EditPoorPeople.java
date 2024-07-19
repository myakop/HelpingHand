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

// class for edit poor people
public class EditPoorPeople extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        // timeButton1 = findViewById(R.id.timeButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editpoorpeople);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Name");
        String message2 = intent.getStringExtra("adress");  // take the data of the poor person  that i want to update
        String message3 = intent.getStringExtra("phone");
        EditText name = findViewById(R.id.nameOfpoor);
        EditText adress = findViewById(R.id.location);
        EditText phone = findViewById(R.id.phoneNumber);
        name.setText(message);
        adress.setText(message2);
        phone.setText(message3);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() { // // set action for the tool bar
            @Override
            public void onClick(View v) {  // home button
                Intent intent = new Intent(EditPoorPeople.this, admin.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // logout button and go to login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(EditPoorPeople.this, homepage.class);
                startActivity(intent);

            }
        });
    }

    // submit edit the poor people
    public void SumbitAdd(View v) {
        PoorPeopleDatabase data = new PoorPeopleDatabase(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Edit")                                               // set confirm alert
                .setMessage("Are you sure you want to Edit this record?")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { //  canceling button
                        Intent intent2 = getIntent();
                      int id =  intent2.getIntExtra("id" , 0 );
                      data.deletePoorPerson(id,v);

                        EditText name = findViewById(R.id.nameOfpoor) ;
                        EditText location = findViewById(R.id.location) ;
                        EditText phone = findViewById(R.id.phoneNumber) ;
                        String name2 = name.getText().toString();
                        String location2 = location.getText().toString();
                        String phoneNumber2 = phone.getText().toString();
                        data.insertPoorPerson(name2,location2,phoneNumber2,v,2);
                        editfFragment dialogFragment = new editfFragment();
                        dialogFragment.show(getSupportFragmentManager(), "example_dialog");



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // canceling button
                        Intent intent = getIntent();
                        String message = intent.getStringExtra("Name");
                        String message2 = intent.getStringExtra("adress");
                        String message3 = intent.getStringExtra("phone");
                        EditText name = findViewById(R.id.nameOfpoor);
                        EditText adress = findViewById(R.id.location);
                        EditText phone = findViewById(R.id.phoneNumber);
                        name.setText(message);
                        adress.setText(message2);
                        phone.setText(message3);
                        Intent intent2 = new Intent(EditPoorPeople.this, admin.class);
                        startActivity(intent2);


                    }
                })
                .setCancelable(false)
                .show();

    }
}
