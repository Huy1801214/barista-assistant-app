package com.edu.server.api;

import com.edu.server.collection.ProductEntity;
import com.edu.server.dao.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<ProductEntity>> getProducts(
            @RequestHeader("Authorization") String token,
            @RequestParam(name = "categoryId", required = false) String categoryId) {
        List<ProductEntity> products;
        if (categoryId != null && !categoryId.isEmpty()) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAll();
        }

        return ResponseEntity.ok(products);
    }
}