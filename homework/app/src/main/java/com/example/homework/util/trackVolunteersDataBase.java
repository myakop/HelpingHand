package com.example.homework.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// database for trackVolunteers  using sqllite and firestore
public class trackVolunteersDataBase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "track_volunteers.db";
    private static final int DATABASE_VERSION = 4;
    private static final String TABLE_NAME = "volunteers12";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ORGANIZATION = "nameoforg";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_TASK_COMPLETED = "taskcompleted";
    private static final String COLUMN_FIRESTORE_ID = "firestore_id";

    private CollectionReference volunteersCollection;
    private Context context;
    private FirebaseFirestore firestore;
    private ArrayAdapter<String> track;

    public trackVolunteersDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        volunteersCollection = firestore.collection("volunteersTrack");


    }
    public trackVolunteersDataBase(Context context, ArrayAdapter<String> track  ) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        this.track = track;
        volunteersCollection = firestore.collection("volunteersTrack");

        addFirestoreRealtimeListener(track);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_ORGANIZATION + " TEXT NOT NULL, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DATE + " TEXT NOT NULL, "
                + COLUMN_TIME + " TEXT NOT NULL, "
                + COLUMN_TASK_COMPLETED + " TEXT NOT NULL, "  // Add a space before TEXT
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

    private void addFirestoreRealtimeListener( ArrayAdapter<String> track ) {
        // Add a real-time listener to the volunteers collection
        volunteersCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed: " + e);
                return;
            }

            // Handle the changes (e.g., delete the volunteer in SQLite)
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
                        String organization = dc.getDocument().getString("nameOfOrg");
                        String name = dc.getDocument().getString("name");
                        String date = dc.getDocument().getString("date");
                        String time = dc.getDocument().getString("time");
                        String taskCompleted = dc.getDocument().getString("taskCompleted");

                        if(!isVolunteerExist(organization, name, date, time, taskCompleted)) {
                            addVolunteer2(organization, name, date, time, taskCompleted, firestoreId);
                        }

                    }
                }
                List<String> itemList = getAllVolunteers2();
                Log.d("wsel", "aladd ");
                track.clear(); // Clear the existing data in the ArrayAdapter
                track.addAll(itemList); // Add the new itemList to the ArrayAdapter
                track.notifyDataSetChanged();
            }
        });
    }

    private void removeVolunteerByFirestoreId(String firestoreId) {
        Log.d("VolunteerDataBase", "Removing volunteer with Firestore ID: " + firestoreId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_FIRESTORE_ID + " = ?", new String[]{firestoreId});
        db.close();
    }

    public long addVolunteer(String organization, String name, String date, String time, String taskCompleted) {
        // Save to SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORGANIZATION, organization);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_TASK_COMPLETED, taskCompleted);
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();

        // Save to Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference volunteersCollection = firestore.collection("volunteersTrack");

        trackvolunteer volunteer = new trackvolunteer(organization, name, date, time, taskCompleted);
        volunteersCollection.add(volunteer)
                .addOnSuccessListener(documentReference -> {
                    String firestoreId = documentReference.getId();
                    // Save the Firestore ID to the SQLite database for future reference if needed
                    updateFirestoreId(insertedId, firestoreId);
                })
                .addOnFailureListener(e -> {
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

    public long addVolunteer2(String organization, String name, String date, String time, String taskCompleted, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORGANIZATION, organization);
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_TASK_COMPLETED, taskCompleted);
        values.put(COLUMN_FIRESTORE_ID, firestoreId); // Add Firestore ID to the ContentValues
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();
        return insertedId;
    }

    public Cursor getAllVolunteers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    public void removeAllVolunteers() {
        // Remove all volunteers from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


    public List<String> getAllVolunteers2() {
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String organization = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORGANIZATION));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));
            String time = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            int taskCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TASK_COMPLETED));

            String item = "Organization: " + organization + ", Name: " + name + ", Date: " + date
                    + ", Time: " + time + ", Task Completed: " + taskCompleted;
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }

    public void DeleteVolunteer(int id) {
        // Get the Firestore ID associated with the SQLite row
        String firestoreId = getFirestoreId(id);

        // Remove from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Remove from Firestore if the Firestore ID exists
        if (firestoreId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference volunteersCollection = firestore.collection("track_volunteers");
            volunteersCollection.document(firestoreId).delete()
                    .addOnSuccessListener(aVoid -> {
                        Log.d("Firestore", "Volunteer deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
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

    public void getAllVolunteers3() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference volunteersCollection = firestore.collection("volunteersTrack");

        volunteersCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                String firestoreId = document.getId(); // Get the Firestore ID
                String organization = document.getString("nameOfOrg");
                String name = document.getString("name");
                String date = document.getString("date");
                String time = document.getString("time");
                String taskCompleted = document.getString("taskCompleted");

                Log.e("Firestore", "faaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaat");

             if(!isVolunteerExist(organization, name, date, time, taskCompleted)) {
                 // Pass the Firestore ID along with the data to addVolunteer2 method
                 addVolunteer2(organization, name, date, time, taskCompleted, firestoreId);
             }
            }

        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting volunteers from Firestore: " + e.getMessage());
        });
    }

    private boolean isVolunteerExist(String organization, String name, String date, String time, String taskCompleted) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ORGANIZATION + "=? AND " + COLUMN_NAME + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_TIME + "=? AND " + COLUMN_TASK_COMPLETED + "=?",
                new String[]{organization, name, date, time, taskCompleted}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }



    public void removeAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }



}
