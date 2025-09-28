package com.example.gateway.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.gateway.exceptions.handler.WebFluxExceptionHandler;

/**
 * <p>
 * 网关配置类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/30 17:43
 */

@Configuration
public class GatewayAutoConfiguration {

    /**
     * webFlux异常处理器（包括gateway）
     *
     * @return
     */
    @Bean
    public WebFluxExceptionHandler globalWebFluxExceptionHandler() {
        return new WebFluxExceptionHandler();
    }

}
