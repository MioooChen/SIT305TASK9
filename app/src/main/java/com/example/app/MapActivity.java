package com.example.app;

import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<ItemLocation> placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        placesList = new ArrayList<>();
        for (Item item : Database.getItems()) {
            placesList.add(item.getLocation());
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (placesList != null) {
            LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

            for (ItemLocation place : placesList) {
                LatLng latLng = new LatLng(place.getLatitude(), place.getLongitude());
                if (latLng != null) {
                    mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    boundsBuilder.include(latLng);
                }
            }

            LatLngBounds bounds = boundsBuilder.build();
            int padding = 100;
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
        }
    }

}
