package com.example.homework.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
// database for donations money using sqllite and firestore
public class financialDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "donation.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "donors";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DONATION = "donation";
    public static final String COLUMN_FIRESTORE_ID = "firestore_id";

    private CollectionReference donorsCollection;
    private Context context;
    private FirebaseFirestore firestore;
    private  ArrayAdapter<String> donationAdapter;

    public financialDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        donorsCollection = firestore.collection("donors");


    }
    public financialDatabase(Context context, ArrayAdapter<String> donationAdapter) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.donationAdapter=donationAdapter;
        firestore = FirebaseFirestore.getInstance();
        donorsCollection = firestore.collection("donors");
        addFirestoreRealtimeListener(donationAdapter);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DONATION + " REAL NOT NULL, "
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
    private void addFirestoreRealtimeListener(ArrayAdapter<String> donationAdapter) {

        donorsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed: " + e);
                return;
            }

            if (queryDocumentSnapshots != null) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String firestoreId = dc.getDocument().getId();
                        if (!processedFirestoreIds.contains(firestoreId)) {
                            // Remove the donor with matching Firestore ID from local SQLite
                            removeDonorByFirestoreId(firestoreId);
                            processedFirestoreIds.add(firestoreId);
                        }
                    }
                    else if (dc.getType() == DocumentChange.Type.ADDED) {
                        String firestoreId = dc.getDocument().getId(); // Get the Firestore ID
                        String name = dc.getDocument().getString("name");
                        double donation = dc.getDocument().getDouble("donation");


                        if(!isDonorExist(name,donation)) {
                            addDonor2(name, donation,firestoreId);
                        }
                    }
                }
                List<String> itemList = getAllDonors2();
                donationAdapter.clear(); // Clear the existing data in the ArrayAdapter
                donationAdapter.addAll(itemList); // Add the new itemList to the ArrayAdapter
                donationAdapter.notifyDataSetChanged();
            }
        });
    }
    // remove from sqlite
    private void removeDonorByFirestoreId(String firestoreId) {
        Log.d("financialDatabase", "Removing donor with Firestore ID: " + firestoreId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_FIRESTORE_ID + " = ?", new String[]{firestoreId});
        db.close();
    }
    // add to sqlite and firestore
    public long addDonor(String name, double donation , View view , int x) {
        // Save to SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DONATION, donation);
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();

        // Save to Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference donorsCollection = firestore.collection("donors");

        Donor donor = new Donor(name, donation);
        donorsCollection.add(donor)
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
                    showSnackbar(view , "field to add to firebase");
                    Log.e("Firestore", "Error adding donor to Firestore: " + e.getMessage());
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
    public long addDonor2(String name, double donation, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DONATION, donation);
        values.put(COLUMN_FIRESTORE_ID, firestoreId); // Add Firestore ID to the ContentValues
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();
        return insertedId;
    }
    // get all  from the sqlite
    public Cursor getAllDonors() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    // get all like strings from the sqlite
    public List<String> getAllDonors2() {
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            double donation = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_DONATION));

            String item = "Name: " + name + ", Donation: " + donation;
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
    // delete from the firestore
    public void DeleteDonation(int id , View view) {
        // Get the Firestore ID associated with the SQLite row
        String firestoreId = getFirestoreId(id);

        // Remove from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Remove from Firestore if the Firestore ID exists
        if (firestoreId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference donorsCollection = firestore.collection("donors");
            donorsCollection.document(firestoreId).delete()
                    .addOnSuccessListener(aVoid -> {
                        showSnackbar(view , "success to remove from firebase");
                        Log.d("Firestore", "Donor deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        showSnackbar(view , "feild to remove from firebase");
                        Log.e("Firestore", "Error deleting donor from Firestore: " + e.getMessage());
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
    public void getAllDonors3() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference donorsCollection = firestore.collection("donors");

        donorsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                String firestoreId = document.getId(); // Get the Firestore ID
                String name = document.getString("name");
                double donation = document.getDouble("donation");

                if (!isDonorExist(name, donation)) {
                    // Pass the Firestore ID along with the data to addDonor2 method
                    addDonor2(name, donation, firestoreId);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting donors from Firestore: " + e.getMessage());
        });
    }
    // check if exist
    private boolean isDonorExist(String name, double donation) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "=? AND " + COLUMN_DONATION + "=?",
                new String[]{name, String.valueOf(donation)}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
