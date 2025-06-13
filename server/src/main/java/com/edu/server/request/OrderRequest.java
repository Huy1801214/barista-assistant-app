package com.edu.server.request;

import java.util.List;

public class OrderRequest {
    private String createAt;
    private List<OrderItem> orderItems;
    private double totalPrice;
    private double discount;
    private String voucher;
    private String storeId;
    private String note;

    public OrderRequest() {}

    public OrderRequest(String createAt, List<OrderItem> orderItems, double totalPrice, double discount, String voucher, String note, String storeId) {
        this.createAt = createAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.voucher = voucher;
        this.note = note;
        this.storeId = storeId;
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

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public void setNote(String note) {
        this.note = note;
    }
}
