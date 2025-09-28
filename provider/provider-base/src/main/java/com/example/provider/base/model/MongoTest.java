package com.example.provider.base.model;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.mongo.provider.stater.model.BaseMongoModel;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Document("mongo_test")
public class MongoTest extends BaseMongoModel<Long> {

    @Indexed
    private Long uid;

    private String name;

}
