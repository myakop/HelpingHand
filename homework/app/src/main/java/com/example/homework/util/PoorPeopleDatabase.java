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
// database for PoorPeople  using sqllite and firestore
public class PoorPeopleDatabase extends SQLiteOpenHelper {

    private static final String COLUMN_FIRESTORE_ID = "firestore_id";
    private static final String DATABASE_NAME = "poor_people.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_NAME = "poor_people";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_PHONE = "phone";
    private  ArrayAdapter<String> poorpeopleAdater;

    private CollectionReference poorPeopleCollection;
    private Context context;
    private FirebaseFirestore firestore;

    public PoorPeopleDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        poorPeopleCollection = firestore.collection("poor_people");


    }
    public PoorPeopleDatabase(Context context , ArrayAdapter<String> poorpeopleAdater) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        this.poorpeopleAdater = poorpeopleAdater;
        poorPeopleCollection = firestore.collection("poor_people");

        addFirestoreRealtimeListener(poorpeopleAdater);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_ADDRESS + " TEXT NOT NULL, "
                + COLUMN_PHONE + " TEXT NOT NULL, "
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
    private void addFirestoreRealtimeListener(ArrayAdapter<String> poorpeopleAdater) {

        poorPeopleCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed: " + e);
                return;
            }


            if (queryDocumentSnapshots != null) {
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String firestoreId = dc.getDocument().getId();
                        if (!processedFirestoreIds.contains(firestoreId)) {
                            // Remove the poor person with matching Firestore ID from local SQLite
                            removePoorPersonByFirestoreId(firestoreId);
                            processedFirestoreIds.add(firestoreId);
                        }
                    }
                    else if (dc.getType() == DocumentChange.Type.ADDED) {
                        String firestoreId = dc.getDocument().getId(); // Get the Firestore ID
                        String name = dc.getDocument().getString("name");
                        String address = dc.getDocument().getString("address");
                        String phone = dc.getDocument().getString("phone");

                        if(!isPoorPersonExist(name,address,phone)) {
                            insertPoorPerson2(name, address,phone, firestoreId);
                        }
                    }
                }
                List<String> itemList = getAllPoorPeople2();
                poorpeopleAdater.clear(); // Clear the existing data in the ArrayAdapter
                poorpeopleAdater.addAll(itemList); // Add the new itemList to the ArrayAdapter
                poorpeopleAdater.notifyDataSetChanged();
            }
        });
    }
    // remove from sqlite
    private void removePoorPersonByFirestoreId(String firestoreId) {
        Log.d("PoorPeopleDatabase", "Removing poor person with Firestore ID: " + firestoreId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_FIRESTORE_ID + " = ?", new String[]{firestoreId});
        db.close();
    }
    // add to sqlite and firestore
    public long insertPoorPerson(String name, String address, String phone, View view,int x) {
        // Save to SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHONE, phone);
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();

        // Save to Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference poorPeopleCollection = firestore.collection("poor_people");

        PoorPeople poorPerson = new PoorPeople(name, address, phone);
        poorPeopleCollection.add(poorPerson)
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
                    Log.e("Firestore", "Error adding poor person to Firestore: " + e.getMessage());
                });

        return insertedId;
    }
    // add just to sqlite
    public long insertPoorPerson2(String name, String address, String phone, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_FIRESTORE_ID, firestoreId); // Add Firestore ID to the ContentValues
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();
        return insertedId;
    }

    public void updateFirestoreId(long rowId, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRESTORE_ID, firestoreId);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(rowId)});
        db.close();
    }
    // get all the data from sqllite
    public Cursor getAllPoorPeople() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    // get all like strings from the sqlite
    public List<String> getAllPoorPeople2() {
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE));

            String item = "Name: " + name + ", Address: " + address + ", Phone: " + phone;
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
    // delete from the firestore
    public void deletePoorPerson(int id , View view) {
        // Get the Firestore ID associated with the SQLite row
        String firestoreId = getFirestoreId(id);

        // Remove from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Remove from Firestore if the Firestore ID exists
        if (firestoreId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference poorPeopleCollection = firestore.collection("poor_people");
            poorPeopleCollection.document(firestoreId).delete()
                    .addOnSuccessListener(aVoid -> {
                        showSnackbar(view , "success to remove from firebase");
                        Log.d("Firestore", "Poor person deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        showSnackbar(view , "feild to remove from firebase");

                        Log.e("Firestore", "Error deleting poor person from Firestore: " + e.getMessage());
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
    public void getAllPoorPeople3() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference poorPeopleCollection = firestore.collection("poor_people");

        poorPeopleCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                String firestoreId = document.getId(); // Get the Firestore ID
                String name = document.getString("name");
                String address = document.getString("address");
                String phone = document.getString("phone");

                if (!isPoorPersonExist(name, address, phone)) {
                    // Pass the Firestore ID along with the data to insertPoorPerson method
                    insertPoorPerson2(name, address, phone, firestoreId);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting poor people from Firestore: " + e.getMessage());
        });
    }

    // check if exist
    private boolean isPoorPersonExist(String name, String address, String phone) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "=? AND " + COLUMN_ADDRESS + "=? AND " + COLUMN_PHONE + "=?",
                new String[]{name, address, phone}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}
