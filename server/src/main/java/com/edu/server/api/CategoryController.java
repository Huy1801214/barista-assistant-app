package com.edu.server.api;

import com.edu.server.collection.CategoryEntity;
import com.edu.server.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<List<CategoryEntity>> getAllCategories(@RequestHeader("Authorization") String token) {
        // Tương tự, trong thực tế sẽ lấy các category theo storeId từ token
        List<CategoryEntity> categories = categoryRepository.findAll();
        return ResponseEntity.ok(categories);
    }
}