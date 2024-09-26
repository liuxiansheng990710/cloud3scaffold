package com.example.commons.web.servlet.response;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * <p>
 * 返回体特殊处理注解
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 16:53
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Wrapper {

    /**
     * 是否需要进行包装
     */
    boolean wrapper() default false;

    /**
     * 自定义状态码
     */
    HttpStatus httpStatus() default HttpStatus.OK;

}
