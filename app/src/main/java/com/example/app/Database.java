package com.example.app;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static List<Item> items;
    private static final String FILE_PATH = "items.json";

    private static Context context;

    public static void init(Context context) {
        Database.context = context;
        loadItemsFromFile();
    }


    private static void loadItemsFromFile() {
        items = new ArrayList<>();
        try (FileInputStream fileInputStream = context.openFileInput(FILE_PATH);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            Gson gson = new Gson();
            Type listType = new com.google.gson.reflect.TypeToken<List<Item>>() {}.getType();
            items = gson.fromJson(stringBuilder.toString(), listType);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void saveItemsToFile() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(items);
            FileOutputStream fileOutputStream = context.openFileOutput(FILE_PATH, Context.MODE_PRIVATE);
            fileOutputStream.write(json.getBytes());
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Item> getItems() {
        return items;
    }

    public static void addItem(Item item) {
        items.add(item);
        saveItemsToFile();

    }

    public static void removeItem(Item item) {
        items.remove(item);
        saveItemsToFile();
    }
}