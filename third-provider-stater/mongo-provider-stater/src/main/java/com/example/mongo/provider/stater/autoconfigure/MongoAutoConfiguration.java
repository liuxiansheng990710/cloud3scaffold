package com.example.mongo.provider.stater.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.CustomConversions;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import com.example.mongo.provider.stater.config.DateToMongoDateConvert;
import com.example.mongo.provider.stater.config.MongoCompositeKeyFillCallback;
import com.example.mongo.provider.stater.config.MongoOperatorAuditorAware;
import com.example.mongo.provider.stater.metrics.MongoMetricsFactoryBean;
import com.example.mongo.provider.stater.properties.MongoConfigProperties;
import com.mongodb.ConnectionString;

/**
 * <p>
 * mongo自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 11:24
 */

@Configuration
@ConditionalOnClass(MongoTemplate.class)
@EnableConfigurationProperties(MongoConfigProperties.class)
//matchIfMissing：如果配置文件没有添加该配置是否加载bean 配置为true，因为如果没配置的话，可以默认是开启
@ConditionalOnProperty(name = MongoConfigProperties.MONGO_CONFIG_PROPERTIES + ".enable", matchIfMissing = true)
public class MongoAutoConfiguration {

    /**
     * mongo id配置替换
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MongoConfigProperties.MONGO_CONFIG_PROPERTIES + ".keyFillCallback", matchIfMissing = true)
    public MongoCompositeKeyFillCallback mongoCompositeKeyFillListener() {
        return new MongoCompositeKeyFillCallback();
    }

    /**
     * mongo 操作人信息自动填充
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MongoConfigProperties.MONGO_CONFIG_PROPERTIES + ".auditorAware", matchIfMissing = true)
    public MongoOperatorAuditorAware operatorAuditorAware() {
        return new MongoOperatorAuditorAware();
    }

    /**
     * mongo 类型转换器
     */
    @Bean
    @ConditionalOnProperty(name = MongoConfigProperties.MONGO_CONFIG_PROPERTIES + ".convert", matchIfMissing = true)
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory factory, MongoMappingContext context) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        List<Converter<?, ?>> converters = new ArrayList<>();
        // 添加自定义的转换器（此处只针对于时间类型转换）
        converters.add(new DateToMongoDateConvert());
        CustomConversions customConversions = new CustomConversions(CustomConversions.StoreConversions.NONE, converters);
        mappingConverter.setCustomConversions(customConversions);
        // 将mongo数据中的_class字段去掉
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        return mappingConverter;
    }

    /**
     * mongo 日志工厂
     *
     * @param properties
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MongoConfigProperties.MONGO_CONFIG_PROPERTIES + ".log", matchIfMissing = true)
    public MongoMetricsFactoryBean mongoLogFactoryBean(MongoProperties properties) {
        MongoMetricsFactoryBean mongoLogFactoryBean = new MongoMetricsFactoryBean();
        mongoLogFactoryBean.setConnectionString(new ConnectionString(properties.getUri()));
        return mongoLogFactoryBean;
    }

}
