package com.edu.server.dao;

import com.edu.server.collection.StoreEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends MongoRepository<StoreEntity, String> {

    /**
     * Tìm cửa hàng dựa trên ID của người sở hữu (owner).
     * @param ownerId ID của người dùng có vai trò là OWNER
     * @return Một Optional chứa Store nếu tìm thấy.
     */
    Optional<StoreEntity> findByOwnerId(String ownerId);
}

