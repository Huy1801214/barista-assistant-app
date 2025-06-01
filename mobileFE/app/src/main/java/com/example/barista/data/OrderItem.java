package com.example.barista.data;

public class OrderItem {
    private String itemName;
    private double itemPrice;

    public OrderItem(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getItemName() {
        return itemName;
    }
}
