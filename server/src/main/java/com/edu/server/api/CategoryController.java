package com.edu.server.api;

import com.edu.server.collection.CategoryEntity;
import com.edu.server.dao.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<CategoryEntity> createCategory(@RequestBody CategoryEntity category) {
        // TODO: Gán storeId cho category mới
        CategoryEntity savedCategory = categoryRepository.save(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<CategoryEntity> updateCategory(@PathVariable String id, @RequestBody CategoryEntity categoryDetails) {
        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(categoryDetails.getName());
        // ... cập nhật các trường khác nếu có
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'MANAGER')")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        // Cẩn thận: Xóa category có thể cần xóa cả các sản phẩm liên quan
        // TODO: Thêm logic xóa sản phẩm trong CategoryService
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}