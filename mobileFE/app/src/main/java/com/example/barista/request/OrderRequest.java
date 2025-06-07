package com.example.barista.request;

import com.example.barista.data.OrderItem;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class OrderRequest {
    public static class I_OrderItem {
        private int id;
        private String itemName;
        private double itemPrice;
        private int quantity;

        public I_OrderItem(int id, String itemName, double itemPrice, int quantity) {
            this.id = id;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.quantity = quantity;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public double getItemPrice() {
            return itemPrice;
        }

        public void setItemPrice(double itemPrice) {
            this.itemPrice = itemPrice;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    private String createAt;
    private List<OrderRequest.I_OrderItem> orderItems;

    public OrderRequest(LocalDateTime orderTime, List<OrderItem> orderItems) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.createAt = orderTime.format(formatter);
        this.orderItems = new LinkedList<>();

        for (OrderItem item : orderItems) {
            this.orderItems.add(new I_OrderItem(item.getId(), item.getItemName(), item.getItemPrice(), item.getQuantity()));

        }


    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<I_OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<I_OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    public String decodeToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
