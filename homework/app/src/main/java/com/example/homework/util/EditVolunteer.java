package com.example.homework.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Locale;
// class for edit Volunteer hours and work details
public class EditVolunteer extends AppCompatActivity {

    Button timeButton1;
    int hour, minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // timeButton1 = findViewById(R.id.timeButton);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editvolunteers);
        Intent intent = getIntent();
        String message = intent.getStringExtra("Name");
        String message2 = intent.getStringExtra("des");
        String message3  = intent.getStringExtra("time");
        timeButton1 = findViewById(R.id.timeButton);
        timeButton1.setText(message3);

        EditText name = findViewById(R.id.fullname2);          //// take the data of the poor person  that i want to update
        EditText des = findViewById(R.id.desc2);
        name.setText(message);
        des.setText(message2);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {  //  // set action for the tool bar
            @Override
            public void onClick(View v) {  // home button
                Intent intent = new Intent(EditVolunteer.this, admin.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // log out and go to login
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(EditVolunteer.this, homepage.class);
                startActivity(intent);

            }
        });
    }


    public void popTimePicker(View view) // get date
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) // get hours
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton1 = findViewById(R.id.timeButton);
                timeButton1.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // submit edit the volunteers helping
    public void SubmitToAdd(View v) {
        VolunteerDataBase data = new VolunteerDataBase(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Edit")                                              // set confirm alert
                .setMessage("Are you sure you want to Edit this record?")
                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // confirm button
                        Intent intent2 = getIntent();
                        int id = intent2.getIntExtra("id" , 0);
                        data.DeleteVolunteer(id,v);
                        EditText name = findViewById(R.id.fullname2) ;
                        String TheName = name.getText().toString();
                        CalendarView calendarView = findViewById(R.id.calendarView2);
                        // Get the current selected date from the CalendarView
                        long selectedDateMillis = calendarView.getDate();
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.setTimeInMillis(selectedDateMillis);
                        int year = selectedCalendar.get(Calendar.YEAR);
                        int month = selectedCalendar.get(Calendar.MONTH);
                        int dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH);
                        String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;

                        EditText des = findViewById(R.id.desc2) ;
                        String des2 = des.getText().toString();
                        String Time = timeButton1.getText().toString();

                        data.addVolunteer(TheName, selectedDate, Time,des2,v,2);
                        editfFragment dialogFragment = new editfFragment();
                        dialogFragment.show(getSupportFragmentManager(), "example_dialog");


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { // canceling button
                        Intent intent = getIntent();
                        String message = intent.getStringExtra("Name");
                        String message2 = intent.getStringExtra("des");
                        EditText name = findViewById(R.id.fullname2);
                        EditText des = findViewById(R.id.desc2);
                        name.setText(message);
                        des.setText(message2);
                        Intent intent2 = new Intent(EditVolunteer.this, admin.class);
                        startActivity(intent2);


                    }
                })
                .setCancelable(false)
                .show();

    }




}
