package com.example.mongo.provider.stater.metrics;

import org.springframework.data.mongodb.core.MongoClientFactoryBean;

import com.mongodb.MongoClientSettings;

import lombok.NonNull;

/**
 * <p>
 * mongo指标工厂，主要是注册mongo指标监听器
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 11:31
 */

public class MongoMetricsFactoryBean extends MongoClientFactoryBean {

    @Override
    @NonNull
    protected MongoClientSettings computeClientSetting() {
        return MongoClientSettings
                .builder(super.computeClientSetting())
                .addCommandListener(new MongoMetricsListener())
                .build();
    }
}
