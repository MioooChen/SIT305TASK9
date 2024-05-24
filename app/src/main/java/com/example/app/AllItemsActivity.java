package com.example.app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AllItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewItems;
    private ItemAdapter itemAdapter;
    private List<Item> items;

    private ActivityResultLauncher<Intent> itemDetailLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                itemAdapter.notifyDataSetChanged();
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        recyclerViewItems = findViewById(R.id.recyclerViewItems);

        items = Database.getItems();
        itemAdapter = new ItemAdapter(this, items, new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Item item = items.get(position);
                Intent intent = new Intent(AllItemsActivity.this, ItemDetailActivity.class);
                intent.putExtra("item", item);
                intent.putExtra("position", position);
                itemDetailLauncher.launch(intent);
            }
        });

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);
    }

}
