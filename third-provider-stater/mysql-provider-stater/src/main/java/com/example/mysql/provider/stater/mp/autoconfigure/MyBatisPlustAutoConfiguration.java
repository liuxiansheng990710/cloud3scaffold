package com.example.mysql.provider.stater.mp.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;

/**
 * <p>
 * mp自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 17:11
 */

@Configuration
public class MyBatisPlustAutoConfiguration {

    /**
     * mp 分页插件
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mysqlInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return interceptor;
    }

}
