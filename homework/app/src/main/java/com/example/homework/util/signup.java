package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.homework.R;
import com.example.homework.util.sqLite;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.nullness.qual.NonNull;

// sign app page
public class signup extends Activity {
    sqLite databaseHelper = new sqLite(this);
    Button submitSignUp;
    FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        mAuth = FirebaseAuth.getInstance();
        submitSignUp = findViewById(R.id.button5);
    }

    // submit signup button
    public void goLogIn(View v) {
        EditText nameEditText = findViewById(R.id.editTextText);
        EditText passEditText = findViewById(R.id.editTextTextPassword2);
        EditText phoneEditText = findViewById(R.id.editTextPhone);
        EditText mailEditText = findViewById(R.id.MailUser);

        RadioButton TakeGender;
        if (findViewById(R.id.radioF).isSelected()) {
            TakeGender = findViewById(R.id.radioF);
        } else {
            TakeGender = findViewById(R.id.radioM);
        }

        String name = nameEditText.getText().toString();
        String password = passEditText.getText().toString();
        String phoneNumber = phoneEditText.getText().toString();
        String gender = TakeGender.getText().toString();
        String email = mailEditText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password) // create a account in the fire store
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            databaseHelper.open();
                            databaseHelper.insertUser(nameEditText, passEditText, phoneEditText, TakeGender, mailEditText);
                            databaseHelper.close();
                            Intent intent = new Intent(signup.this, login.class);
                            startActivity(intent);
                        } else {
                            // User creation failed
                            Exception exception = task.getException();
                            Log.e("FirebaseAuth", "Error creating user: " + exception.getMessage());

                        }
                    }
                });
    }
}
