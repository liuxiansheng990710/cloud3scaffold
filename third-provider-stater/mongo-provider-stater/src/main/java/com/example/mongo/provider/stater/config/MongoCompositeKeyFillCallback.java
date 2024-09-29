package com.example.mongo.provider.stater.config;

import java.util.Objects;

import org.springframework.core.Ordered;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertCallback;

import com.example.mongo.provider.stater.model.BaseMongoModel;

import cn.hutool.core.lang.Snowflake;
import lombok.NonNull;

/**
 * <p>
 * 1. 对于手动未填充的ID字段则使用默认雪花ID填充
 * </p>
 * <p>
 * 2. 将id填充执行条件设置为最后，保证其他审计字段正确填充
 * </P>
 *
 * @author : 21
 * @since : 2024/9/29 11:23
 */

public class MongoCompositeKeyFillCallback implements BeforeConvertCallback<BaseMongoModel>, Ordered {

    /**
     * 为了@CreatedDate、@CreatedBy审计字段正确填充，故id字段最后填充
     * 因为先填充id会出现审计字段无法填充的情况，所以@CreatedDate、@CreatedBy被执行的条件是id为空
     *
     * @return
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public BaseMongoModel onBeforeConvert(BaseMongoModel entity, @NonNull String collection) {
        if (Objects.isNull(entity.getId())) {
            Snowflake snowflake = new Snowflake();
            entity.setId(snowflake.nextId());
        }
        return entity;
    }
}
