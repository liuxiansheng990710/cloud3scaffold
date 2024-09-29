package com.example.mongo.provider.stater.service;

import java.util.Optional;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.UpdateDefinition;

import com.mongodb.client.result.UpdateResult;

/**
 * <p>
 * 基础mongo操作方法
 * </p>
 *
 * @param <ID> 表实体ID类型
 * @param <T>  表实体
 * @author : 21
 * @since : 2024/9/29 15:23
 */

public interface MongoService<T, ID> {

    /**
     * 保存
     *
     * @param entity 表实体(可以是子类)
     * @param <S>    extends T
     * @return 表实体
     */
    <S extends T> S save(S entity);

    <S extends T> Iterable<S> saveAll(Iterable<S> entities);

    UpdateResult upsert(Query query, UpdateDefinition update, Class<?> entityClass);

    T findByQuery(Query query, Class<T> entityClass);

    Optional<T> findById(ID id);

    boolean existsById(ID id);

    Iterable<T> findAll();

    Iterable<T> findAllById(Iterable<ID> ids);

    long count();

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Iterable<? extends T> entities);

    void deleteAll();

}
