package edu.sjsu.android.hw4;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class LocationsContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "edu.sjsu.android.hw4.locations"; // Content Provider’s name
    static final String URL = "content://" + PROVIDER_NAME + "/locations";
    static final Uri CONTENT_URI = Uri.parse(URL); // A URI for Content Provider to do operations on location table
    static final int LOCATIONS = 1;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "locations", LOCATIONS);
    }

    /**
     * Database specific constant declarations
     */
    private LocationsDB db;

    @Override
    public boolean onCreate() {
        /** Create a write able database which will trigger its creation if it doesn't already exist. */
        db = new LocationsDB(getContext());
        return (db == null) ? false : true;
    }

    @Override
    // A callback method that is invoked when insert operation is requested on this content provider
    public Uri insert(Uri uri, ContentValues values) {
        /** Add a new location record */
        long rowID = db.insertLocation(values);
        /** If record is added successfully */
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new android.database.SQLException("Failed to add a record into " + uri);
    }

    @Override
    // A callback method that is invoked by default content URI
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (uriMatcher.match(uri) == LOCATIONS) return db.returnAllLocations();
        return null;
    }

    @Override
    // A callback method that is invoked when delete operation is requested on this content provider
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.deleteAllLocations();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}