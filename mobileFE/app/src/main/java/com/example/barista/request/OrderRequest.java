package com.example.barista.request;

import com.example.barista.data.OrderItem;
import com.google.gson.Gson;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class OrderRequest {
    public static class RequestOrderItem {
        private String id;
        private String itemName;
        private double itemPrice;
        private int quantity;


        public RequestOrderItem(String id, String itemName, double itemPrice, int quantity) {
            this.id = id;
            this.itemName = itemName;
            this.itemPrice = itemPrice;
            this.quantity = quantity;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
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
    private List<RequestOrderItem> orderItems;
    private double totalPrice;
    private double discount;
    private String voucher;
    private String storeId;
    private String note;

    public OrderRequest(LocalDateTime orderTime, List<OrderItem> orderItems) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.createAt = orderTime.format(formatter);
        this.orderItems = new LinkedList<>();

        for (OrderItem item : orderItems) {
            this.orderItems.add(new RequestOrderItem(item.getId(), item.getItemName(), item.getItemPrice(), item.getQuantity()));

        }
    }

    public OrderRequest(LocalDateTime orderTime, List<OrderItem> orderItems, double totalPrice, double discount, String voucher, String note, String storeId) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        this.createAt = orderTime.format(formatter);
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.voucher = voucher;
        this.storeId = storeId;
        this.orderItems = new LinkedList<>();
        this.note = note;

        for (OrderItem item : orderItems) {
            this.orderItems.add(new RequestOrderItem(item.getId(), item.getItemName(), item.getItemPrice(), item.getQuantity()));

        }
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<RequestOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<RequestOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getNote() {
        return note;
    }
    public String decodeToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
