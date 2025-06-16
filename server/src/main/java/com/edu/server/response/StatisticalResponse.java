package com.edu.server.response;

import com.edu.server.collection.OrderEntity;

import java.util.List;

public class StatisticalResponse {
    private int orderCount;
    private double revenue;
    private double discount;
    private int discountCount;
    private List<OrderEntity> history;
    private double total;

    public StatisticalResponse() {

    }
    public StatisticalResponse(int orderCount, double revenue, double discount, List<OrderEntity> history, double total, int  discountCount) {
        this.orderCount = orderCount;
        this.revenue = revenue;
        this.discount = discount;
        this.history = history;
        this.total = total;
        this.discountCount = discountCount;
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getDiscountCount() {
        return discountCount;
    }

    public void setDiscountCount(int discountCount) {
        this.discountCount = discountCount;
    }
}
