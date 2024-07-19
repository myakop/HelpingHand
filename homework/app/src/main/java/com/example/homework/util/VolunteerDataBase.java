package com.example.homework.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// database for Volunteers  using sqllite and firestore
public class VolunteerDataBase extends SQLiteOpenHelper {

    private static final String COLUMN_FIRESTORE_ID = "firestore_id";
    private static final String DATABASE_NAME = "volunteers23.db";
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "volunteers";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_DES = "Description";

    private CollectionReference volunteersCollection;
    private Context context;
    private FirebaseFirestore firestore;
    private  ArrayAdapter<String> volunteerAdater;


    // constructor
    public VolunteerDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        volunteersCollection = firestore.collection("volunteers");

    }
    // constructor
    public VolunteerDataBase(Context context , ArrayAdapter<String> volunteerAdater) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.volunteerAdater = volunteerAdater;
        firestore = FirebaseFirestore.getInstance();
        volunteersCollection = firestore.collection("volunteers");
        addFirestoreRealtimeListener(volunteerAdater);


    }
      // create the table sqlite
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_TIME + " TEXT NOT NULL, "
                + COLUMN_DES + " TEXT, "
                + COLUMN_FIRESTORE_ID + " TEXT"
                + ");";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    private Set<String> processedFirestoreIds = new HashSet<>();
     // listener for changers to update it auto
    private void addFirestoreRealtimeListener(ArrayAdapter<String> volunteerAdater) {

        volunteersCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed: " + e);
                return;
            }


            if (queryDocumentSnapshots != null) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String firestoreId = dc.getDocument().getId();
                        if (!processedFirestoreIds.contains(firestoreId)) {
                            // Remove the volunteer with matching Firestore ID from local SQLite
                            removeVolunteerByFirestoreId(firestoreId);
                            processedFirestoreIds.add(firestoreId);
                        }
                    }
                    else if (dc.getType() == DocumentChange.Type.ADDED) {
                        String firestoreId = dc.getDocument().getId(); // Get the Firestore ID
                        String name = dc.getDocument().getString("name");
                        String date = dc.getDocument().getString("date");
                        String time = dc.getDocument().getString("time");
                        String des = dc.getDocument().getString("des");

                        if(!isVolunteerExist(name,date,time)) {
                            addVolunteer2(name, date,time,des, firestoreId);
                        }
                    }
                }
                List<String> itemList = getAllVolunteers2();
                volunteerAdater.clear(); // Clear the existing data in the ArrayAdapter
                volunteerAdater.addAll(itemList); // Add the new itemList to the ArrayAdapter
                volunteerAdater.notifyDataSetChanged();
            }
        });
    }


   // remove from sqlite
    private void removeVolunteerByFirestoreId(String firestoreId) {
        Log.d("VolunteerDataBase", "Removing volunteer with Firestore ID: " + firestoreId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_FIRESTORE_ID + " = ?", new String[]{firestoreId});
        db.close();
    }

    // add to sqlite and firestore
    public long addVolunteer(String name, String date, String time, String des ,View view , int x ) {
        // Save to SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DES, des);
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();

        // Save to Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference volunteersCollection = firestore.collection("volunteers");

        Volunteer volunteer = new Volunteer(name, date, time, des);
        volunteersCollection.add(volunteer)
                .addOnSuccessListener(documentReference -> {
                    String firestoreId = documentReference.getId();
                    // Save the Firestore ID to the SQLite database for future reference if needed
                    updateFirestoreId(insertedId, firestoreId);
                    if(x==1) {
                        showSnackbar(view, "success to add to firebase");
                    }else{
                        showSnackbar(view, "success to Edit");
                    }
                })
                .addOnFailureListener(e -> {
                    showSnackbar(view , "feild to add to firebase");
                    Log.e("Firestore", "Error adding volunteer to Firestore: " + e.getMessage());
                });

        return insertedId;
    }

    public void updateFirestoreId(long rowId, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRESTORE_ID, firestoreId);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(rowId)});
        db.close();
    }

    // add just to sqlite
    public long addVolunteer2(String name, String date, String time, String des, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_DES, des);
        values.put(COLUMN_FIRESTORE_ID, firestoreId); // Add Firestore ID to the ContentValues
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();
        return insertedId;
    }

// get all from the sqlite
    public Cursor getAllVolunteers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
// get all like strings from the sqlite
    public List<String> getAllVolunteers2() {
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            String des = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DES));

            String item = "Name: " + name + ", Date: " + date + ", Time: " + time + ", Des: " + des;
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
// delete from the firestore
    public void DeleteVolunteer(int id , View view) {
        // Get the Firestore ID associated with the SQLite row
        String firestoreId = getFirestoreId(id);

        // Remove from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Remove from Firestore if the Firestore ID exists
        if (firestoreId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference volunteersCollection = firestore.collection("volunteers");
            volunteersCollection.document(firestoreId).delete()
                    .addOnSuccessListener(aVoid -> {
                        showSnackbar(view , "success to remove from firebase");
                        Log.d("Firestore", "Volunteer deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        showSnackbar(view , "feild to remove from firebase");

                        Log.e("Firestore", "Error deleting volunteer from Firestore: " + e.getMessage());
                    });
        }
    }

    private String getFirestoreId(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_FIRESTORE_ID}, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        String firestoreId = null;
        if (cursor.moveToFirst()) {
            firestoreId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FIRESTORE_ID));
        }
        cursor.close();
        return firestoreId;
    }
// get all the data from the firestore
    public void getAllVolunteers3() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference volunteersCollection = firestore.collection("volunteers");

        volunteersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                String firestoreId = document.getId(); // Get the Firestore ID
                String name = document.getString("name");
                String date = document.getString("date");
                String time = document.getString("time");
                String des = document.getString("des");

                if (!isVolunteerExist(name, date, time)) {
                    // Pass the Firestore ID along with the data to addVolunteer2 method
                    addVolunteer2(name, date, time, des ,firestoreId);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting volunteers from Firestore: " + e.getMessage());
        });
    }

// check if exist
    private boolean isVolunteerExist(String name, String date, String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_TIME + "=?",
                new String[]{name, date, time}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
