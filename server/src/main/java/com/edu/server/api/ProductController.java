package com.edu.server.api;

import com.edu.server.collection.ProductEntity;
import com.edu.server.dao.ProductRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    ProductRepository productRepository;
    @GetMapping
    public ResponseEntity<String> getItem(@RequestParam(name = "id", required = false) String id) {
        if (id != null && !id.isEmpty()) {
            ProductEntity e =  productRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
            Gson gson = new Gson();
            String json = gson.toJson(e);
            return ResponseEntity.ok(json);
        }

        List<ProductEntity> ps = productRepository.findAll();
        Gson gson = new Gson();
        String json = gson.toJson(ps);
        return ResponseEntity.ok(json);
    }
}
