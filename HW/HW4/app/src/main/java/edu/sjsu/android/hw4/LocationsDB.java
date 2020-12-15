package edu.sjsu.android.hw4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class LocationsDB extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "LocationMarkers";  // a. Database name
    static final String TABLE_NAME = "locations";           // b. Table name
    static final String _ID = "_id";                        // c. Field 1: primary key
    static final String LATITUDE = "latitude";              // d. Field 2: latitude
    static final String LONGITUDE = "longitude";             // e. Field 3: longitude
    static final String ZOOM = "zoom";                  // f. Field 4: zoom level of the map
    static final int DATABASE_VERSION = 1;
    // LatLng is actually a public final double: https://developers.google.com/android/reference/com/google/android/gms/maps/model/LatLng
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " latitude DOUBLE NOT NULL, " +
                    " longitude DOUBLE NOT NULL, " +
                    " zoom INTEGER NOT NULL);";
    private SQLiteDatabase db;

    public LocationsDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // A method that inserts a new location to the table
    public long insertLocation(ContentValues values) {
        // Insert the new row, returning the primary key value of the new row
        long rowID = db.insert(TABLE_NAME, "", values);
        return rowID;

    }

    // A method that deletes all locations from the table
    public int deleteAllLocations() {
        int count = db.delete(TABLE_NAME, null, null);
        return count;
    }

    // A method that returns all the locations from the table
    public Cursor returnAllLocations() {
        // https://developer.android.com/training/data-storage/sqlite#java
        Cursor c = db.query(
                TABLE_NAME, // The table to query
                null, // The array of columns to return (pass null to get all)
                null, // The columns for the WHERE clause
                null, // The values for the WHERE clause
                null, // don't group the rows
                null, // don't filter by row groups
                null); // The sort order
        return c;
    }
}