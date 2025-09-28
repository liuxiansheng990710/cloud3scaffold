package com.example.provider.base.model;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.mongo.provider.stater.service.MongoService;
import com.example.mongo.provider.stater.service.impl.MongoServiceImpl;

@Service
public class MongoTestService extends MongoServiceImpl<MongoTest, Long, MongoTestRepository> implements MongoService<MongoTest, Long> {

    public Page<MongoTest> get(Pageable pageable, String name) {
        return baseRepository.findByNameOrderByUidAsc(pageable, name);
    }

}
