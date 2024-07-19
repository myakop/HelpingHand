package com.example.homework.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.Activity;
import android.widget.TextView;

import com.example.homework.R;
import com.google.firebase.auth.FirebaseAuth;


import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;
// page to donate time(set the hours and date and description (skills) that the user can help

public class physicaldonation extends AppCompatActivity {
    private String mActivityName;
    Button timeButton1;
    int hour, minute;
    TextView mtextView;
    Button mbutton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.physicaldonation);
        timeButton1 = findViewById(R.id.timeButton);
        ImageView home  = findViewById(R.id.homeButton);
        ImageView logout  = findViewById(R.id.logout);
        home.setOnClickListener(new View.OnClickListener() {  // set action for the tool bar
            @Override
            public void onClick(View v) { // home button
                Intent intent = new Intent(physicaldonation.this, enterpage.class);
                startActivity(intent);

            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // logout button and logout
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(physicaldonation.this, homepage.class);
                startActivity(intent);

            }
        });
    }
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton1.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    // submit add new volunteer time and help
    public void SubmitToAdd(View v) {
        VolunteerDataBase data = new VolunteerDataBase(this);
        EditText name = findViewById(R.id.fullname) ;
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

        EditText des = findViewById(R.id.desc) ;
        String des2 = des.getText().toString();


        String Time = timeButton1.getText().toString();
        data.addVolunteer(TheName, selectedDate, Time,des2,v,1);

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
  // button that go to view organzition and description
    public void ViewOrganaztion(View v) {
        Intent intent = new Intent(physicaldonation.this, ViewOrganization.class);
        startActivity(intent);
    }










}
