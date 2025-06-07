package com.edu.server.collection;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
public class OrderEntity {
    @Id
    private String id;
    private String createdAt;
    private OrderItemEntity[] orderItemEntities;

    public OrderEntity() {}

    public OrderEntity(String createdAt, OrderItemEntity[] orderItemEntities) {
        this.createdAt = createdAt;
        this.orderItemEntities = orderItemEntities;
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


}
