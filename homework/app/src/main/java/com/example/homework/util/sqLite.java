package com.example.homework.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
 // data
public class sqLite extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "user.db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names
    private static final String TABLE_NAME = "users";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_PHONE_NUMBER = "phone_number";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_BIRTHDAY = "birthday";

    private FirebaseFirestore db;
    private CollectionReference usersCollection;

    public sqLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = FirebaseFirestore.getInstance();
        usersCollection = db.collection(TABLE_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        // Create the local SQLite database table
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_PASSWORD + " TEXT NOT NULL, "
                + COLUMN_PHONE_NUMBER + " TEXT NOT NULL, "
                + COLUMN_GENDER + " TEXT NOT NULL, "
                + COLUMN_BIRTHDAY + " TEXT"
                + ");";
        database.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // Drop older table if existed
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(database);
    }

    public void open() {
        SQLiteDatabase database = getWritableDatabase();
    }

    public void close() {
        SQLiteDatabase database = getWritableDatabase();
        if (database != null && database.isOpen()) {
            database.close();
        }
    }

    public void insertUser(EditText nameEditText, EditText passwordEditText,
                           EditText phoneNumberEditText, RadioButton genderEditText,
                           EditText birthdayEditText) {
        String name = nameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String gender = genderEditText.getText().toString();
        String birthday = birthdayEditText.getText().toString();

        // Create a new user document in Firestore
        User user = new User(name, password, phoneNumber, gender, birthday);
        usersCollection.add(user);
    }


}
