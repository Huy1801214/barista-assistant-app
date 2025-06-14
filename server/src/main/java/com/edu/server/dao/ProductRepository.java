package com.edu.server.dao;


import com.edu.server.collection.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductEntity, String> {

    /**
     * Lấy tất cả sản phẩm của một cửa hàng.
     * @param storeId ID của cửa hàng
     * @return Danh sách các Product của cửa hàng đó.
     */
    List<ProductEntity> findByStoreId(String storeId);

    /**
     * Lấy tất cả sản phẩm thuộc một danh mục cụ thể của một cửa hàng.
     * Rất hữu ích khi người dùng click vào một danh mục trên menu.
     * @param storeId ID của cửa hàng
     * @param categoryId ID của danh mục
     * @return Danh sách các Product thỏa mãn điều kiện.
     */
    List<ProductEntity> findByStoreIdAndCategoryId(String storeId, String categoryId);
    List<ProductEntity> findByCategoryId(String categoryId);
}
