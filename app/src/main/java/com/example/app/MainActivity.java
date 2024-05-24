package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;


import com.google.android.libraries.places.api.Places;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Places.initialize(getApplicationContext(), getMetaDataValue("com.google.android.geo.API_KEY"));

        Database.init(this);

        Button createButton = findViewById(R.id.button1);
        createButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, NewAdvertActivity.class);
            startActivity(intent);
        });

        Button viewButton = findViewById(R.id.button2);
        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllItemsActivity.class);
            startActivity(intent);
        });

        Button showMapButton = findViewById(R.id.button3);
        showMapButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MapActivity.class);
            startActivity(intent);
        });



    }

    private String getMetaDataValue(String name) {
        try {
            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

}