package com.example.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;


import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class NewAdvertActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextDescription, editTextDate, editTextLocation;

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private ActivityResultLauncher<Intent> autocompleteLauncher;

    private FusedLocationProviderClient fusedLocationClient;
    private PlacesClient placesClient;

    private Place location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        placesClient = Places.createClient(this);

        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextDate = findViewById(R.id.editTextDate);
        editTextLocation = findViewById(R.id.editTextLocation);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveAdvert());

        Button buttonGetLocation = findViewById(R.id.buttonGetCurrentLocation);
        buttonGetLocation.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }

            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);

            LocationServices.getFusedLocationProviderClient(this).getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(Task<Location> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        Location loc = task.getResult();
                        LatLng latLng = new LatLng(loc.getLatitude(), loc.getLongitude());

                        Geocoder geocoder = new Geocoder(NewAdvertActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                Address address = addresses.get(0);
                                String placeName = address.getFeatureName();
                                placeName += ", " + address.getThoroughfare();
                                placeName += ", " + address.getLocality();

                                // 更新Place对象和编辑文本
                                Place place = Place.builder().setName(placeName).setLatLng(latLng).build();
                                location = place;
                                editTextLocation.setText(placeName);
                            } else {
                                Log.e("MainActivity", "Failed to get current location");
                                Toast.makeText(NewAdvertActivity.this, "Failed to get current location, try using autocomplete.", Toast.LENGTH_SHORT).show();
                            }

                            progressBar.setVisibility(View.GONE);
                        } catch (IOException e) {
                            Log.e("MainActivity", "Geocoder failed", e);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(NewAdvertActivity.this, "Failed to get current location, try using autocomplete.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("MainActivity", "Failed to get current location");
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(NewAdvertActivity.this, "Failed to get current location, try using autocomplete.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

         });


        autocompleteLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        System.out.println("Place: " + place.getName() + ", " + place.getId());
                        editTextLocation.setText(place.getName());
                        this.location = place;
                    } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        Status status = Autocomplete.getStatusFromIntent(result.getData());
                        System.out.println(status.getStatusMessage());
                    }
                });

    }


    private void saveAdvert() {
        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String description = editTextDescription.getText().toString();
        String date = editTextDate.getText().toString();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Phone is required");
            return;
        }

        if (TextUtils.isEmpty(description)) {
            editTextDescription.setError("Description is required");
            return;
        }

        if (TextUtils.isEmpty(date)) {
            editTextDate.setError("Date is required");
            return;
        }

        if (this.location == null) {
            editTextLocation.setError("Location is required");
            return;
        }


        RadioGroup radioGroupPostType = findViewById(R.id.radioGroupPostType);
        int selectedId = radioGroupPostType.getCheckedRadioButtonId();
        RadioButton radioButtonSelected = findViewById(selectedId);
        String postType = radioButtonSelected.getText().toString();

        Item item = new Item((postType.equals("Lost") ? Item.PostType.LOST : Item.PostType.FOUND), name, phone, description, date, new ItemLocation(location));
        Database.addItem(item);
        Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void showDatePickerDialog(View v) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth1) -> {
                    String date = (month1 + 1) + "/" + dayOfMonth1 + "/" + year1;
                    editTextDate.setText(date);
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }

    public void showMapPickerDialog(View v) {
        // Set the fields to specify which types of place data to return
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);

        // Start the autocomplete intent
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(NewAdvertActivity.this);
        autocompleteLauncher.launch(intent);
    }
}
