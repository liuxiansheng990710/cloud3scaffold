package com.example.mongo.provider.stater.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.lang.Nullable;

import com.example.mongo.provider.stater.service.MongoService;
import com.mongodb.client.result.UpdateResult;

/**
 * <p>
 * 基础Mongo方法实现类，封装公用方法
 * </p>
 *
 * @param <T>  表实体
 * @param <ID> 表Id类型
 * @param <R>  mongo操作数据层
 * @author : 21
 * @since : 2024/9/29 15:23
 */

public class MongoServiceImpl<T, ID, R extends MongoRepository<T, ID>> implements MongoService<T, ID> {

    @Autowired
    protected R baseRepository;
    @Autowired
    protected MongoTemplate mongoTemplate;

    @Override
    public <S extends T> S save(S entity) {
        return baseRepository.save(entity);
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return baseRepository.saveAll(entities);
    }

    @Override
    public Optional<T> findById(ID id) {
        return baseRepository.findById(id);
    }

    @Override
    public boolean existsById(ID id) {
        return baseRepository.existsById(id);
    }

    @Override
    public Iterable<T> findAll() {
        return baseRepository.findAll();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return baseRepository.findAllById(ids);
    }

    @Override
    public long count() {
        return baseRepository.count();
    }

    @Override
    public void deleteById(ID id) {
        baseRepository.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        baseRepository.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        baseRepository.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        baseRepository.deleteAll();
    }

    @Override
    public UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass) {
        return mongoTemplate.upsert(query, update, entityClass);
    }

    @Override
    @Nullable
    public T findByQuery(Query query, Class<T> entityClass) {
        return mongoTemplate.findOne(query, entityClass);
    }

}
