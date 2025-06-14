package com.example.barista.response;

import com.example.barista.data.OrderEntity;

import java.util.List;

public class StatisticalResponse {
    private int orderCount;
    private double revenue;
    private double discount;
    private List<OrderEntity> history;

    public StatisticalResponse() {

    }
    public StatisticalResponse(int orderCount, double revenue, double discount, List<OrderEntity> history) {
        this.orderCount = orderCount;
        this.revenue = revenue;
        this.discount = discount;
        this.history = history;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public List<OrderEntity> getHistory() {
        return history;
    }

    public void setHistory(List<OrderEntity> history) {
        this.history = history;
    }
}
