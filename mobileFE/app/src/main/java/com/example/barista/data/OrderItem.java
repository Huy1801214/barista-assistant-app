package com.example.barista.data;

public class OrderItem {
    private int id;
    private String itemName;
    private double itemPrice;
    private int quantity;
    private String thumbnailUrl;

    public OrderItem(int id, String itemName, double itemPrice, int quantity, String thumbnailUrl) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
        this.thumbnailUrl = thumbnailUrl;
        this.id = id;

    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public int getId() {
        return id;
    }
}
