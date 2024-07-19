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

// // add poor people
public class AddPoorControll extends AppCompatActivity {

    // view add poor people page
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addpoorpeople);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() { // set action for the tool bar
            @Override
            public void onClick(View v) {  // go to home page(for admin)
                Intent intent = new Intent(AddPoorControll.this, admin.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // sign out and go to login page
                Intent intent = new Intent(AddPoorControll.this, homepage.class);
                startActivity(intent);

            }
        });
    }
    //submait adding new organization
    public void SumbitAdd(View v) {
        PoorPeopleDatabase data = new PoorPeopleDatabase(this);
        EditText name = findViewById(R.id.nameOfpoor) ;
        EditText location = findViewById(R.id.location) ;
        EditText phone = findViewById(R.id.phoneNumber) ;
        String name2 = name.getText().toString();
        String location2 = location.getText().toString();
        String phoneNumber2 = phone.getText().toString();
        data.insertPoorPerson(name2,location2,phoneNumber2,v,1);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // alert that add successfuly
        dialogFragment22 dialogFragment = new dialogFragment22();
        dialogFragment.show(getSupportFragmentManager(), "example_dialog");



    }


}
