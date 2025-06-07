package com.edu.server.api;

import com.edu.server.collection.OrderEntity;
import com.edu.server.collection.OrderItemEntity;
import com.edu.server.dao.OrderRepository;
import com.edu.server.request.OrderItem;
import com.edu.server.request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderAPI {
    private static final int ORDER_CREATE_COMPLETE = 1;
    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<Integer> createOrder(@RequestBody OrderRequest request) {

        OrderItemEntity[] orderItemEntities = new OrderItemEntity[request.getOrderItems().size()];

        for (int i = 0; i < orderItemEntities.length; i++) {
            OrderItem item = request.getOrderItems().get(i);
            orderItemEntities[i] = new OrderItemEntity(
                    item.getId(),
                    item.getItemName(),
                    item.getItemPrice(),
                    item.getQuantity()
            );
        }

        OrderEntity orderEntity = new OrderEntity(
                request.getCreateAt(),
                orderItemEntities
        );

        OrderEntity saved = orderRepository.save(orderEntity);

        return ResponseEntity.ok(ORDER_CREATE_COMPLETE);
    }
}
