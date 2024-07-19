package com.example.homework.util;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;


// class for donate money
public class financialcontribution extends AppCompatActivity {
    private String mActivityName;
    TextView mtextView;
    Button mbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.financialcontribution);
        mActivityName = getString(R.string.app_name);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {      // set action for the tool bar
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(financialcontribution.this, enterpage.class);
                startActivity(intent);  // home button

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    // logout and go to login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(financialcontribution.this, homepage.class);
                startActivity(intent);

            }
        });
    }


    //submit add new donation of money
    public void SumbitDonate(View v) {
        financialDatabase data =  new financialDatabase(this);
        EditText name = findViewById(R.id.nameOfVolunter) ;
        EditText donation = findViewById(R.id.donationAmount) ;
        String nameofthevoluner = name.getText().toString();
        String donation2 = donation.getText().toString();
        double amount = Double.parseDouble(donation2);;
        data.addDonor(nameofthevoluner ,amount,v,1);
        Intent intent = getIntent();
        String receivedData = intent.getStringExtra("checkAdmin");
        if(receivedData.equals("Admin")) {

            dialogFragment22 dialogFragment = new dialogFragment22();
            dialogFragment.show(getSupportFragmentManager(), "example_dialog");
        }
        else{
            AddFragment dialogFragment = new AddFragment();
            dialogFragment.show(getSupportFragmentManager(), "example_dialog");

        }



    }
}