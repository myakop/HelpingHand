package com.example.homework.util;


import android.content.Intent;
import android.os.Bundle;


import android.app.Activity;
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


// the main page for the users

public class enterpage extends AppCompatActivity {
    ImageView info;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enterpage);
        info = findViewById(R.id.info);  // fragment that show details to contact with the managers

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replace(new Info());
            }
        });

        // set action for the tool bar
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        ImageView menu = findViewById(R.id.dropdown_menu);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }                                   // home button
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();   // log out button and go to login page
                Intent intent = new Intent(enterpage.this, homepage.class);
                startActivity(intent);
            }

        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override                         // menu bar
            public void onClick(View v) {
                showmenu(v);

            }
        });
    }

    // method to show the fragments
    private void replace(Fragment fragment) {
        FragmentManager freagmentManger = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = freagmentManger.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment );
        fragmentTransaction.commit();

    }

    // method to manage the menu bar that in the tool bar
    private void showmenu(View v) {
        PopupMenu pop = new PopupMenu(this,v);
        pop.getMenuInflater().inflate(R.menu.menubar2, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.Eh){
                    Intent intent = new Intent(enterpage.this, poopeople.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.money){
                    Intent intent = new Intent(enterpage.this, financialcontribution.class);
                    startActivity(intent);

                }
                if(item.getItemId() == R.id.time){
                    Intent intent = new Intent(enterpage.this, physicaldonation.class);
                    startActivity(intent);

                }
                if(item.getItemId() == R.id.top){
                    Intent intent = new Intent(enterpage.this, topvolunteers.class);
                    startActivity(intent);

                }
                return true;
            }
        });
        pop.show();

    }

    // button that go to show some of our works
    public void startpoopeople(View v) {
        Intent intent = new Intent(enterpage.this, poopeople.class);
        startActivity(intent);
    }

    // button that go to show top volunters
    public void starttopvolunteer(View v) {
        Intent intent = new Intent(enterpage.this, topvolunteers.class);
        startActivity(intent);
    }
    // button that go to donate money
    public void startfinancialcontribution(View v) {
        Intent intent = new Intent(enterpage.this, financialcontribution.class);
        String receivedString = "notAdmin";
        intent.putExtra("checkAdmin" ,receivedString );
        startActivity(intent);
    }

    // button that go to donate time and work
    public void startphysicaldonation(View v) {
        Intent intent = new Intent(enterpage.this, physicaldonation.class);
        Intent intent2= getIntent();
        String receivedString = "notAdmin";
        intent.putExtra("checkAdmin" ,receivedString );
        startActivity(intent);
    }
}