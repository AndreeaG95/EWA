package com.andreea.ewa;

import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;

/**
 * This class is used for passing instances between activities.
 */

public class AppState {

    private static AppState singletonObject;
    private static String userId;

    public static synchronized AppState get() {
        if (singletonObject == null) {
            singletonObject = new AppState();
        }
        return singletonObject;
    }

    // reference to Firebase used for reading and writing data
    private DatabaseReference databaseReference;

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public void setDatabaseReference(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public void setUserId(String id){
        this.userId = id;
    }

    public String getUserId(){
        return userId;
    }
}