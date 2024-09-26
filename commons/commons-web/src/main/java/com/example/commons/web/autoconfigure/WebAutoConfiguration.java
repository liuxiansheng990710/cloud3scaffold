package com.example.commons.web.autoconfigure;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import com.example.commons.core.utils.JacksonUtils;
import com.example.commons.web.servlet.resolver.ServerHandlerExceptionResolver;
import com.example.commons.web.servlet.undertow.UndertowServerFactoryCustomizer;
import com.fasterxml.jackson.databind.ObjectMapper;

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
public class WebAutoConfiguration implements WebMvcConfigurer {

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

    /**
     * RequestContextListener 可以让Spring 能够在不同的线程中共享请求的上下文信息。这对于需要在非控制器类中访问 HTTP 请求信息
     * 监听器会在请求生命周期内自动处理请求的上下文以及清理工作，防止监听器未加载成功，导致非控制器内获取请求上下文失败
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /**
     * 使用自定义全局异常类替换默认全局异常处理
     *
     * @param resolvers
     */
    @Override
    public void extendHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.removeIf(DefaultHandlerExceptionResolver.class::isInstance);
        resolvers.add(new ServerHandlerExceptionResolver());
    }

    /**
     * mvc下 自定义序列化与反序列化
     *
     * @param converters
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(wrapperObjectMapper());
        converters.forEach(stringMessageConvert());
    }

    private static Consumer<HttpMessageConverter<?>> wrapperObjectMapper() {
        return converter -> {
            if (converter instanceof MappingJackson2HttpMessageConverter httpMessageConverter) {
                ObjectMapper objectMapper = httpMessageConverter.getObjectMapper();
                JacksonUtils.initObjectMapper(objectMapper);
                httpMessageConverter.setSupportedMediaTypes(getMediaTypes());
                httpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        };
    }

    private static Consumer<HttpMessageConverter<?>> stringMessageConvert() {
        return converter -> {
            if (converter instanceof StringHttpMessageConverter stringHttpMessageConverter) {
                stringHttpMessageConverter.setSupportedMediaTypes(getMediaTypes());
                stringHttpMessageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        };
    }

    private static List<MediaType> getMediaTypes() {
        return Arrays.asList(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("application/vnd.spring-boot.actuator.v2+json"),
                MediaType.APPLICATION_ATOM_XML,
                MediaType.APPLICATION_FORM_URLENCODED,
                MediaType.APPLICATION_OCTET_STREAM,
                MediaType.APPLICATION_PDF,
                MediaType.APPLICATION_RSS_XML,
                MediaType.APPLICATION_XHTML_XML,
                MediaType.APPLICATION_XML,
                MediaType.IMAGE_GIF,
                MediaType.IMAGE_JPEG,
                MediaType.IMAGE_PNG,
                MediaType.TEXT_HTML,
                MediaType.TEXT_MARKDOWN,
                MediaType.TEXT_PLAIN,
                MediaType.TEXT_XML);
    }

}
