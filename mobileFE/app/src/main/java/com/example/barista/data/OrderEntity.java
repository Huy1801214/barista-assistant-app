package com.example.barista.data;


public class OrderEntity {
    private String id;
    private String createdAt;
    private OrderItemEntity[] orderItemEntities;
    private double totalPrice;
    private double discount;
    private String voucher;
    private String storeId;
    private String note;

    public OrderEntity() {}

    public OrderEntity(String createdAt, OrderItemEntity[] orderItemEntities) {
        this.createdAt = createdAt;
        this.orderItemEntities = orderItemEntities;
    }

    public OrderEntity(String createdAt, OrderItemEntity[] orderItemEntities, double totalPrice, double discount, String voucher, String note, String storeId) {
        this.createdAt = createdAt;
        this.orderItemEntities = orderItemEntities;
        this.totalPrice = totalPrice;
        this.discount = discount;
        this.voucher = voucher;
        this.storeId = storeId;
        this.note = note;
    }

    public OrderItemEntity[] getOrderItemEntities() {
        return orderItemEntities;
    }

    public void setOrderItemEntities(OrderItemEntity[] orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public OrderItemEntity[] getOrderItems() {
        return orderItemEntities;
    }

    public void setOrderItems(OrderItemEntity[] orderItemEntities) {
        this.orderItemEntities = orderItemEntities;
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
