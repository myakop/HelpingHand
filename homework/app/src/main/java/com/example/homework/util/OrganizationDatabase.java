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
// database for Organization  using sqllite and firestore
public class OrganizationDatabase extends SQLiteOpenHelper {



    private static final String DATABASE_NAME = "organization.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "organizations";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_FIRESTORE_ID = "firestore_id";

    private CollectionReference organizationsCollection;
    private Context context;
    private FirebaseFirestore firestore;
    private ArrayAdapter<String> organizationAdapter; // New member variable

    public OrganizationDatabase(Context context, ArrayAdapter<String> organizationAdapter) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.organizationAdapter = organizationAdapter; // Store the adapter
        firestore = FirebaseFirestore.getInstance();
        organizationsCollection = firestore.collection("organizations");

        addFirestoreRealtimeListener(organizationAdapter);
    }
    public OrganizationDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        firestore = FirebaseFirestore.getInstance();
        organizationsCollection = firestore.collection("organizations");

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT NOT NULL, "
                + COLUMN_DESCRIPTION + " TEXT, "
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
    public void addFirestoreRealtimeListener(ArrayAdapter<String> organizationAdapter) {
        // Add a real-time listener to the organizations collection
        organizationsCollection.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                Log.e("Firestore", "Listen failed: " + e);
                return;
            }
            // Handle the changes (e.g., delete the organization in SQLite)
            if (queryDocumentSnapshots != null) {
                List<Organization> updatedOrganizations = new ArrayList<>();
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                        String firestoreId = dc.getDocument().getId();
                        if (!processedFirestoreIds.contains(firestoreId)) {
                            Log.d("wsel", "alremove ");
                            // Remove the organization with matching Firestore ID from local SQLite
                            removeOrganizationByFirestoreId(firestoreId);
                            processedFirestoreIds.add(firestoreId);
                        }
                    }
                    else if (dc.getType() == DocumentChange.Type.ADDED) {
                        String firestoreId = dc.getDocument().getId();
                        String name = dc.getDocument().getString("name");
                        String description = dc.getDocument().getString("des");
                        Organization organization = new Organization(name, description);
                        if(!isOrganizationExist(name,description)) {
                            insertOrganization2(name, description, firestoreId);
                        }
                    }
                }
                List<String> itemList = getAllOrganization2();
                for (String item : itemList) {
                    Log.d("ItemList", item);
                }
                Log.d("wsel", "aladd ");
                organizationAdapter.clear(); // Clear the existing data in the ArrayAdapter
                organizationAdapter.addAll(itemList); // Add the new itemList to the ArrayAdapter
                organizationAdapter.notifyDataSetChanged();

            }
        });
    }
    // remove from sqlite
    private void removeOrganizationByFirestoreId(String firestoreId) {
        Log.d("OrganizationDatabase", "Removing organization with Firestore ID: " + firestoreId);
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_FIRESTORE_ID + " = ?", new String[]{firestoreId});
        db.close();
    }
    // add to sqlite and firestore
    public void insertOrganization(String name, String description ,View view , int x) {
        // Save to SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        long insertedId = db.insert(TABLE_NAME, null, values);
        db.close();

        // Save to Firestore
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference organizationsCollection = firestore.collection("organizations");

        Organization organization = new Organization(name, description);
        organizationsCollection.add(organization)
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
                    Log.e("Firestore", "Error adding organization to Firestore: " + e.getMessage());
                });
    }

    public void updateFirestoreId(long rowId, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIRESTORE_ID, firestoreId);
        db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(rowId)});
        db.close();
    }
    // add just to sqlite
    public void insertOrganization2(String name, String description, String firestoreId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_FIRESTORE_ID, firestoreId); // Add Firestore ID to the ContentValues
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    // get all from the sqlite
    public Cursor getAllOrganization() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME, null, null, null, null, null, null);
    }
    // get all like strings from the sqlite
    public List<String> getAllOrganization2() {
        List<String> itemList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));

            String item = "Name: " + name + ", Description: " + description;
            itemList.add(item);
        }

        cursor.close();
        return itemList;
    }
    // delete from the firestore
    public void DeleteOrganization(int id,View view) {
        // Get the Firestore ID associated with the SQLite row
        String firestoreId = getFirestoreId(id);

        // Remove from SQLite database
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();

        // Remove from Firestore if the Firestore ID exists
        if (firestoreId != null) {
            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            CollectionReference organizationsCollection = firestore.collection("organizations");
            organizationsCollection.document(firestoreId).delete()
                    .addOnSuccessListener(aVoid -> {
                        showSnackbar(view , "success to remove from firebase");
                        Log.d("Firestore", "Organization deleted from Firestore");
                    })
                    .addOnFailureListener(e -> {
                        showSnackbar(view , "feild to remove from firebase");
                        Log.e("Firestore", "Error deleting organization from Firestore: " + e.getMessage());
                    });
        }
    }
    // get all the data from the firestore
    public void getAllOrganization3() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference organizationsCollection = firestore.collection("organizations");

        organizationsCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot document : documents) {
                String firestoreId = document.getId(); // Get the Firestore ID
                String name = document.getString("name");
                String description = document.getString("des");

                if (!isOrganizationExist(name, description)) {
                    // Pass the Firestore ID along with the data to insertOrganization2 method
                    insertOrganization2(name, description, firestoreId);
                }
            }
        }).addOnFailureListener(e -> {
            Log.e("Firestore", "Error getting organizations from Firestore: " + e.getMessage());
        });
    }
    // check if exist
    private boolean isOrganizationExist(String name, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_NAME + "=? AND " + COLUMN_DESCRIPTION + "=?",
                new String[]{name, description}, null, null, null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
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

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
