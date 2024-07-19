package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

public class admin extends AppCompatActivity {

    Button button , button2,button3,button4 ;

     // view admin main page (home page for admin)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminhome);

        //buttons for every fragment
        button = findViewById(R.id.VolunteersMangment);
        button2 = findViewById(R.id.org);
        button3 = findViewById(R.id.poor);
        button4 = findViewById(R.id.donations);

        button.setOnClickListener(new View.OnClickListener() { // show fragment that show  the volunteers with the time they can work
            @Override
            public void onClick(View v) {
                replace(new MangamentVolunteer());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() { // show fragment of the Organization
            @Override
            public void onClick(View v) {
                replace(new ManagementOrganization());
            }
        });
        button3.setOnClickListener(new View.OnClickListener() { // show fragment of the PoorPeople
            @Override
            public void onClick(View v) {
                replace(new ManagementPoorPeople());
            }
        });
        button4.setOnClickListener(new View.OnClickListener() { //// show fragment of the Donations
            @Override
            public void onClick(View v) {
                replace(new ViewDonation());
            }
        });

        // set action for the tool bar
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        ImageView menu = findViewById(R.id.dropdown_menu);
        home.setOnClickListener(new View.OnClickListener() {  // go to home page(for admin)
            @Override
            public void onClick(View v) {

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut(); // sign out and go to login page
                Intent intent = new Intent(admin.this, homepage.class);
                startActivity(intent);

            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmenu(v);

            }
        });

    }
    // method that replace the fragments
    private void replace(Fragment fragment) {
        FragmentManager freagmentManger = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = freagmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment );
        fragmentTransaction.commit();

    }
// menu that exist in the toolbar
    private void showmenu(View v) {
        PopupMenu pop = new PopupMenu(this,v);
        pop.getMenuInflater().inflate(R.menu.menubar, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.TopVolunteer){
                    Intent intent = new Intent(admin.this, topvolunteers.class);
                    startActivity(intent);

                }
                if(item.getItemId() == R.id.about){
                    Intent intent = new Intent(admin.this, about.class);
                    startActivity(intent);

                }
                if(item.getItemId() == R.id.logout){
                    FirebaseAuth.getInstance().signOut(); // sign out and go to login page
                    Intent intent = new Intent(admin.this, login.class);
                    startActivity(intent);
                }

                return true;
            }
        });
        pop.show();

    }


}
