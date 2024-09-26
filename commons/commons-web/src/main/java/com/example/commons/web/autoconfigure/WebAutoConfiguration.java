package com.example.commons.web.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.example.commons.web.servlet.undertow.UndertowServerFactoryCustomizer;

import io.undertow.Undertow;

/**
 * <p>
 * web自动配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/24 17:25
 */

@ConditionalOnWebApplication
@EnableWebMvc
@Configuration
public class WebAutoConfiguration {

    /**
     * web servlet undertow
     *
     * @return
     */
    @Bean
    @ConditionalOnClass(Undertow.class)
    public UndertowServerFactoryCustomizer undertowServerFactoryCustomizer() {
        return new UndertowServerFactoryCustomizer();
    }

}
