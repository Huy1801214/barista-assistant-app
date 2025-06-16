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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}