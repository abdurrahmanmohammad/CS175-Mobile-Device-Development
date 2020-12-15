package edu.sjsu.android.hw4;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {
    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onClick_CS(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18);
        map.animateCamera(update);
    }

    public void onClick_Univ(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        map.animateCamera(update);

    }

    public void onClick_City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LoaderManager.getInstance(this).initLoader(0, null, this); // Invoke LoaderCallbacks to retrieve and draw already saved locations in map

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                // Add a maker to the map
                map.addMarker(new MarkerOptions().position(point).title("Find Me Here!"));
                // Creating an instance of LocationInsertTask
                LocationInsertTask insertTask = new LocationInsertTask();
                // Storing the latitude, longitude and zoom level to SQList database
                ContentValues values = new ContentValues();
                values.put(LocationsDB.LATITUDE, point.latitude);
                values.put(LocationsDB.LONGITUDE, point.longitude);
                values.put(LocationsDB.ZOOM, map.getCameraPosition().zoom);
                insertTask.execute(values);
                // Display "Maker is added to the Map" message
                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();
            }
        });

        map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {
                // Removing all markers from the Google Map
                map.clear();
                // Creating an instance of LocationDeleteTask
                LocationDeleteTask deleteTask = new LocationDeleteTask();
                // Deleting all the rows from SQLite database table
                deleteTask.execute();
                // Display “ALL makers are removed" message
                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show();
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Loader<Cursor> c = null;
        // Uri, to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;
        // Fetches all the rows from locations table
        c = new CursorLoader(this, uri, null, null, null, null);
        return c;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> arg0, Cursor arg1) {
        int locationCount = 0;
        double lat = 0;
        double lng = 0;
        float zoom = 0;
        // Number of locations available in the SQLite database table
        if (arg1 != null) {
            locationCount = arg1.getCount();
            // Move the current record pointer to the first row of the table
            arg1.moveToFirst();
        } else {
            locationCount = 0;
        }
        for (int i = 0; i < locationCount; i++) {
            // Get the latitude
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.LATITUDE));
            // Get the longitude
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.LONGITUDE));
            // Get the zoom level
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.ZOOM));
            // Creating an instance of LatLng to plot the location in Google Maps
            LatLng location = new LatLng(lat, lng);
            // Drawing the marker in the Google Maps
            map.addMarker(new MarkerOptions().position(location).title("Marker added!"));
            // Traverse the pointer to the next row
            arg1.moveToNext();
        }
        if (locationCount > 0) {
            // Moving CameraPosition to last clicked position
            map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lng)));
            // Setting the zoom level in the map on last position is clicked
            map.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            // Setting up values to insert the clicked location into SQLite database
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // Deleting all the location stored in SQLite database
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }
}
