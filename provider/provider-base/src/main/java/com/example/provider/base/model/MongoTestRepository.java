package com.example.provider.base.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoTestRepository extends MongoRepository<MongoTest, Long> {

    Page<MongoTest> findByNameOrderByUidAsc(Pageable pageable, String name);

}
