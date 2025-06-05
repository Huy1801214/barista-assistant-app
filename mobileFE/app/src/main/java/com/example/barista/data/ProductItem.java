package com.example.barista.data;

public class ProductItem {
    private int itemId;
    private String itemName;
    private double itemPrice;
    private String thumbnailUrl;

    public ProductItem(int itemId, String itemName, double itemPrice, String thumbnailUrl) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getItemId() {
        return itemId;
    }
}
