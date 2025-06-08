package com.edu.server.dao;


import com.edu.server.collection.CategoryEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends MongoRepository<CategoryEntity, String> {

    /**
     * Lấy tất cả danh mục sản phẩm của một cửa hàng.
     * Đây là truy vấn cơ bản để hiển thị menu.
     * @param storeId ID của cửa hàng
     * @return Danh sách các Category của cửa hàng đó.
     */
    List<CategoryEntity> findByStoreId(String storeId);
}
