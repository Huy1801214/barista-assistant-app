package com.example.barista.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    public static final String ID = "cart";
    private Map<ProductItem, Integer> items;

    public Cart() {
        items = new HashMap<>();
    }

    public void addNewItem(ProductItem item) {
        if (items.containsKey(item)) {
            items.put(item, items.get(item) + 1);
        } else {
            items.put(item, 1);
        }
    }

    public void modifyItem(ProductItem item, int quantity) {
        if (items.containsKey(item)) {
            items.put(item, quantity);
        }
    }

    public int getQuantity(ProductItem item) {
        if (items.containsKey(item)) {
            return items.get(item);
        }
        return 0;
    }

    public void removeItem(ProductItem item) {
        if (items.containsKey(item)) {
            if (items.get(item) > 1) {
                items.put(item, items.get(item) - 1);
            } else {
                items.remove(item);
            }
        }
    }

    public void removeItem(String itemId) {
        for (Map.Entry<ProductItem, Integer> entry : items.entrySet()) {
            if (entry.getKey().getItemId().equals(itemId)) {
                items.remove(entry.getKey());
                break;
            }
        }
    }

    public ProductItem getItem(String itemId) {
        for (Map.Entry<ProductItem, Integer> entry : items.entrySet()) {
            if (entry.getKey().getItemId().equals(itemId)) {
                return entry.getKey();
            }
        }

        return null;
    }

    public Map<ProductItem, Integer> getItems() {
        return items;
    }

    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItems = new ArrayList<>();

        for (Map.Entry<ProductItem, Integer> entry : items.entrySet()) {
            ProductItem item = entry.getKey();
            int quantity = entry.getValue();

            OrderItem orderItem = new OrderItem(item.getId(), item.getName(), item.getItemPrice(), quantity, item.getThumbnailUrl());
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    public double getPrice() {
        double totalPrice = 0.0;
        for (Map.Entry<ProductItem, Integer> entry : items.entrySet()) {
            ProductItem item = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += item.getItemPrice() * quantity;
        }
        return totalPrice;
    }


    public void clear() {
        items.clear();
    }

    public int getTotalItemAmount() {
        int totalAmount = 0;

        for (Map.Entry<ProductItem, Integer> entry : items.entrySet()) {
            ProductItem item = entry.getKey();
            int quantity = entry.getValue();
            totalAmount += quantity;
        }

        return totalAmount;
    }
}
