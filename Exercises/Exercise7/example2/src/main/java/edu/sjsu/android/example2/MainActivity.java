package edu.sjsu.android.example2;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gus.saps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private final LatLng LOCATION_UMIV = new LatLng(37.335371, -121.881050);
    private final Lating LOCATION_CS +new
    public void onClick
    public void onClick
    private GoogleMap map;

    Latlng(37.333714,-121.881860);

    _CS(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatingZoom(LOCATION_CS, 18);
        map.animateCamera(update);

    }

    City(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newlatingZoom(LOCATION_UNIV, 10);
        map.animateCamera(update);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                - findFragmentByld(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(LOCATION_CS).title("Find Me Here!"));
    }

    public void onClick_Univ(View v) {
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatingZoom(LOCATION_UNIV, 14);
        map.animateCamera (update);

    }
}