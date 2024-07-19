package com.example.homework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.homework.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// page for log in into the app
public class login extends Activity {
    private EditText textField;
    private EditText textField2;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        mAuth = FirebaseAuth.getInstance();


    }


    public void strartenterpage(View v) {
        textField = findViewById(R.id.editTextTextEmailAddress);
        String email = textField.getText().toString();
        textField2 = findViewById(R.id.editTextTextPassword);
        String password = textField2.getText().toString();

            // check the user details in the fire store
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (email.equals("admin@hotmail.com")) {
                                    // User is admin, redirect to admin page
                                    Intent intent = new Intent(login.this, admin.class);
                                    intent.putExtra("TheName", email);
                                    startActivity(intent);
                                    return;
                                }
                                // user in an oganzaiton
                                if (email.equals("alslam@gmail.com") || email.equals("foodforall@gmail.com") || email.equals("enjoylearn@gmail.com")) {
                                    // User is admin, redirect to admin page
                                    Intent intent = new Intent(login.this, matchAndTrack.class);
                                    intent.putExtra("TheName", email);
                                    startActivity(intent);
                                    return;
                                }
                                else {
                                    // user is a volunteer
                                    // Login success
                                    String content = email; // Use email as the content
                                    Intent intent = new Intent(login.this, enterpage.class);
                                    intent.putExtra("TheName", content);
                                    startActivity(intent);
                                }
                            } else {
                                // Login failed
                                AlertDialog.Builder builder = new AlertDialog.Builder(login.this);
                                builder.setTitle("Alert")
                                        .setMessage("Login failed. Please check your email and password.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Perform any action needed when the "OK" button is clicked
                                            }
                                        })
                                        .setCancelable(false) // Optional: Set whether the dialog can be canceled by pressing outside of it
                                        .show();
                            }
                        }
                    });
        }


    public void finishActivityA(View v) {
        login.this.finish();
    }
}
