package com.example.mysql.provider.stater.mp.autoconfigure;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.IllegalSQLInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.example.mysql.provider.stater.mp.injector.ColumnsCheckInjector;
import com.example.mysql.provider.stater.mp.metahandler.CustomMetaObjectHandler;
import com.example.mysql.provider.stater.mp.properties.MyBatisPlusConfigProperties;

/**
 * <p>
 * mp自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 17:11
 */

@Configuration
@EnableConfigurationProperties(MyBatisPlusConfigProperties.class)
@ConditionalOnProperty(name = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES + ".enable", matchIfMissing = true)
public class MyBatisPlustAutoConfiguration {

    /**
     * mp 拦截器
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES + ".interceptor", matchIfMissing = true)
    public MybatisPlusInterceptor mysqlInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件拦截器
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        //非法SQL拦截器
        interceptor.addInnerInterceptor(new IllegalSQLInnerInterceptor());
        //防止全表更新删除拦截器
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        return interceptor;
    }

    /**
     * mp id自增
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES + ".id", matchIfMissing = true)
    public IdentifierGenerator identifierGenerator() {
        return new DefaultIdentifierGenerator(RandomUtils.nextLong(0, 31), RandomUtils.nextLong(0, 31));
    }

    /**
     * mp 自动填充
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(name = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES + ".auditorAware", matchIfMissing = true)
    public CustomMetaObjectHandler metaObjectHandler() {
        return new CustomMetaObjectHandler();
    }

    @Bean
    @ConditionalOnProperty(name = MyBatisPlusConfigProperties.MP_CONFIG_PROPERTIES + ".data", matchIfMissing = true)
    public ColumnsCheckInjector columnsCheckInjector() {
        return new ColumnsCheckInjector();
    }

}
