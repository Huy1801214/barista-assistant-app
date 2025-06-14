package com.edu.server.dao;

import com.edu.server.collection.OrderEntity;
import com.edu.server.collection.ProductEntity;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface StatisticalRepository extends MongoRepository<OrderEntity, String> {
    @Query("{createdAt: {$gte: ?0, $lte: ?1 }}")
    List<OrderEntity> getOrderHistoryBtw(String form, String to);
    @Query("{createdAt: {$gte: ?0}}")
    List<OrderEntity> getOrderHistoryFrom(String form);

    @Query("{createdAt: {$lte: ?0}}")
    List<OrderEntity> getOrderHistoryTo(String to);
}
