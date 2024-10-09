package com.example.mysql.provider.stater.mp.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES)
public class MyBatisPlusConfigProperties {

    public static final String MP_CONFIG_PROPERTIES = "mybatis-plus.custom";

    /**
     * 是否启用mybatis-plus 自定义配置
     */
    private boolean enable = true;

    /**
     * 是否启用mybatis-plus 自定义拦截器配置
     */
    private boolean interceptor = true;

    /**
     * 是否启用mybatis-plus 操作人信息自动填充
     */
    private boolean auditorAware = true;

    /**
     * 是否启用mybatis-plus id自动配置
     */
    private boolean id = true;

    /**
     * 是否启用mybatis-plus 数据库检测
     */
    private boolean data = true;

    /**
     * 使用启用mybatis-plus 自定义日志
     */
    private boolean log = true;

}
