package com.edu.server.response;

import com.edu.server.collection.OrderEntity;

import java.util.List;

public class StatisticalResponse {
    private int orderCount;
    private double revenue;
    private double avgOfDay;
    private double discount;
    private List<OrderEntity> history;

}
