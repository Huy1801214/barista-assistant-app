package com.example.barista;

import com.example.barista.data.OrderItem;
import com.example.barista.request.OrderRequest;
import com.example.barista.service.OrderApi;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ApiTest {
    @Test
    public void testApiConnection() throws IOException {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(new OrderItem(1, "Item 1", 10.0, 2, ""));
        orderItems.add(new OrderItem(2, "Item 2", 15.0, 1, ""));

        OrderRequest orderRequest = new OrderRequest(LocalDateTime.now(), orderItems);
        boolean com = OrderApi.createOrder(orderRequest);

        assert com;
    }
}
