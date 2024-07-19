package com.example.homework.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.homework.R;
//about page
public class about extends Activity {
    private String mActivityName;
    TextView mtextView;
    Button mbutton;


    // view page about the app
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        mActivityName = getString(R.string.app_name);
    }
}