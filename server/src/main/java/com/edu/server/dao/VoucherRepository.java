package com.edu.server.dao;

import com.edu.server.collection.VoucherEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepository extends MongoRepository<VoucherEntity, String> {

    // Tìm tất cả voucher của một cửa hàng
    List<VoucherEntity> findByStoreId(String storeId);

    // Kiểm tra xem mã voucher đã tồn tại trong cửa hàng chưa
    boolean existsByStoreIdAndCode(String storeId, String code);

    @Query("{'store_id' : ?0, 'code': ?1}")
    List<VoucherEntity> findByCode(String storeId, String code);
}