package com.example.barista.data;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
}