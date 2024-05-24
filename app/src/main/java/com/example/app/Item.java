package com.example.app;

import androidx.annotation.Nullable;

import com.google.android.libraries.places.api.model.Place;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

class ItemLocation implements Serializable {
    @SerializedName("Latitude")
    private double latitude;

    @SerializedName("Longitude")
    private double longitude;

    @SerializedName("name")
    private String name;

    public ItemLocation(double latitude, double longitude, String name) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public ItemLocation(Place place) {
        this(place.getLatLng().latitude, place.getLatLng().longitude, place.getName());
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        ItemLocation location = (ItemLocation) obj;
        return Double.compare(location.latitude, latitude) == 0 &&
                Double.compare(location.longitude, longitude) == 0 &&
                Objects.equals(name, location.name);

    }
}

public class Item implements Serializable {
    public enum PostType {
        @SerializedName("Lost")
        LOST,
        @SerializedName("Found")
        FOUND
    }

    @SerializedName("PostType")
    private PostType postType;

    @SerializedName("Name")
    private String name;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Description")
    private String description;

    @SerializedName("Date")
    private String date;

    @SerializedName("Location")
    private ItemLocation location;

    public Item(PostType postType, String name, String phone, String description, String date, ItemLocation location) {
        this.postType = postType;
        this.name = name;
        this.phone = phone;
        this.description = description;
        this.date = date;
        this.location = location;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ItemLocation getLocation() {
        return location;
    }

    public void setLocation(ItemLocation location) {
        this.location = location;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name) &&
                Objects.equals(date, item.date) &&
                Objects.equals(phone, item.phone) &&
                Objects.equals(location, item.location) &&
                Objects.equals(description, item.description) &&
                postType == item.postType;
    }


}
