package com.example.barista.data;

import androidx.annotation.Nullable;

public class CartItem {
    private int itemId;
    private String itemName;
    private double itemPrice;
    private String thumbnailUrl;

    public CartItem(int itemId, String itemName, double itemPrice, String thumbnailUrl) {
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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof CartItem) {
            CartItem other = (CartItem) obj;
            return itemId == other.itemId;
        } else {
            return false;
        }
    }
}
