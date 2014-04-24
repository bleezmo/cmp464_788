package com.lehman.android;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {
	GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();
    }
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
            	mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
					
					@Override
					public void onMapClick(LatLng pt) {
						Log.i("Lat/Long", pt.latitude+"/"+pt.longitude);
					}
				});
            	Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(40.758546225130104, -73.975471816957))
                .title("Hello world").draggable(true));
            	mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
					@Override
					public boolean onMarkerClick(Marker marker) {
						Toast.makeText(MainActivity.this, "I've been clicked", Toast.LENGTH_SHORT).show();
						return false;
					}
				});
            	marker.showInfoWindow();
            	mMap.setMyLocationEnabled(true);
            }
        }
    }
}
