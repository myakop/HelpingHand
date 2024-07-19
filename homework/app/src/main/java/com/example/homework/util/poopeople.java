package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

// Activity to show an over view about some projects
public class poopeople extends Activity {
    private String mActivityName;
    TextView mtextView;
    Button mbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.poopeople);
        mActivityName = getString(R.string.app_name);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {  // set action for the tool bar
            @Override
            public void onClick(View v) {  // home page
                Intent intent = new Intent(poopeople.this, enterpage.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // logut button
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(poopeople.this, homepage.class);
                startActivity(intent);

            }
        });
    }
}