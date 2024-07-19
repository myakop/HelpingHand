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
import androidx.fragment.app.FragmentManager;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

// add organization
public class AddOrgaization  extends AppCompatActivity {

      // view organization add page
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addorganaztion);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() { // set action for the tool bar
            @Override
            public void onClick(View v) { // go to home page(for admin)
                Intent intent = new Intent(AddOrgaization.this, admin.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // sign out and go to login page
                Intent intent = new Intent(AddOrgaization.this, homepage.class);
                startActivity(intent);

            }
        });
    }

    //submait adding new organization
    public void SumbitAdd2(View v) {
        OrganizationDatabase data = new OrganizationDatabase(this);
        EditText name = findViewById(R.id.nameOforg) ;
        EditText des = findViewById(R.id.descrptionOrg) ;
        String name2 = name.getText().toString();
        String des2 = des.getText().toString();

        data.insertOrganization(name2,des2,v,1);

        dialogFragment22 dialogFragment = new dialogFragment22(); // alert that add successfuly
        dialogFragment.show(getSupportFragmentManager(), "example_dialog");



    }




}
