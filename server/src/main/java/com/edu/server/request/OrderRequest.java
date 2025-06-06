package com.edu.server.request;

import java.util.List;

public class OrderRequest {
    private String createAt;
    private List<OrderItem> orderItems;
    private double totalPrice;

    public OrderRequest() {}

    public OrderRequest(String createAt, List<OrderItem> orderItems, double totalPrice) {
        this.createAt = createAt;
        this.orderItems = orderItems;
        this.totalPrice = totalPrice;
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
}
