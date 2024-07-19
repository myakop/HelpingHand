package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.widget.TextView;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

// home page
public class homepage extends Activity {
    private String mActivityName;
    TextView mtextView;
    private FirebaseAuth mAuth;
    sqLite databaseHelper = new sqLite(this);
Button mbutton;
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mActivityName=getString(R.string.app_name);
        mtextView = findViewById(R.id.textView);
        mbutton=findViewById(R.id.button2);
        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already authenticated
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // User is already logged in, redirect based on role
            String email = currentUser.getEmail();
            if ("admin@hotmail.com".equals(email)) {
                Intent intent = new Intent(homepage.this, admin.class);
                intent.putExtra("TheName", email);
                startActivity(intent);
                finish();
                return;

            } if ("alslam@gmail.com".equals(email) || "enjoylearn@gmail.com".equals(email) || "foodforall@gmail.com".equals(email) ) {
                Intent intent = new Intent(homepage.this, matchAndTrack.class);
                intent.putExtra("TheName", email);
                startActivity(intent);
                finish();
                return;
            }
            else {
                Intent intent = new Intent(homepage.this, enterpage.class);
                intent.putExtra("TheName", email);
                startActivity(intent);
                finish();
            }
        }
    }

    // go to the login page
    public void startlogin(View v) {
        Intent intent = new Intent(homepage.this, login.class);
        startActivity(intent);
    }

    //go to the about page
    public void strartabout(View v) {
        Intent intent = new Intent(homepage.this, about.class);
        startActivity(intent);
    }
    // go to the sign up page
    public void strartsignup(View v) {
        Intent intent = new Intent(homepage.this, signup.class);
        startActivity(intent);
    }
    public void finishActivityA(View v) {
        homepage.this.finish();
    }

}

