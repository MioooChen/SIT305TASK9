package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ItemDetailActivity extends AppCompatActivity {
    TextView name, time, location;
    Button removeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Item item = (Item) getIntent().getSerializableExtra("item");

        name = findViewById(R.id.textViewName);
        time = findViewById(R.id.textViewTime);
        location = findViewById(R.id.textViewLocation);

        name.setText(item.getPostType() + " " + item.getName());

        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        String itemDateS = item.getDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        System.out.println(itemDateS);
        Date itemDate = null;
        try {
            itemDate = dateFormat.parse(item.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 计算日期差距
        long differenceMillis = currentDate.getTime() - itemDate.getTime();
        long differenceDays = TimeUnit.MILLISECONDS.toDays(differenceMillis);

        if (differenceDays < 3) {
            String daysAgo = (differenceDays == 0) ? "today" : (differenceDays + " day ago");
            time.setText(daysAgo);
        } else {
            time.setText(itemDateS);
        }

        location.setText("At " + item.getLocation().getName());

        removeButton = findViewById(R.id.buttonRemove);
        removeButton.setOnClickListener(v -> {
            Database.removeItem(item);

            finish();
        });
    }


}