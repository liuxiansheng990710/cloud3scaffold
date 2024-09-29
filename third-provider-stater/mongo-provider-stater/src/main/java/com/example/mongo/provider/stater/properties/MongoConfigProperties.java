package com.example.mongo.provider.stater.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = MongoConfigProperties.MONGO_CONFIG_PROPERTIES)
public class MongoConfigProperties {

    public static final String MONGO_CONFIG_PROPERTIES = "spring.data.mongodb.custom";

    /**
     * 是否启用mongo 自定义配置
     */
    private boolean enable = true;

    /**
     * 是否启用mongo id自动填充配置
     */
    private boolean keyFillCallback = true;

    /**
     * 是否启用mongo 操作人信息自动填充
     */
    private boolean auditorAware = true;

    /**
     * 是否启用mongo 自动转换
     */
    private boolean convert = true;

    /**
     * 使用启用mongo 自定义日志
     */
    private boolean log = true;

}
