package com.example.commons.web.servlet.resolver;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.example.commons.web.servlet.exceptions.enums.ExceptionEnum;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

/**
 * <p>
 * 全局异常处理器
 * AbstractHandlerExceptionResolver是Spring 提供的一个抽象基类，用于处理在处理请求过程中发生的异常。异常解析器允许在全局范围内定义异常处理策略
 * {@link HandlerExceptionResolver
 * {@link DispatcherServlet}.
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 14:55
 */

public class ServerHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

    /**
     * 设置为最低优先级，保证是在响应后处理异常
     */
    public ServerHandlerExceptionResolver() {
        setOrder(Ordered.LOWEST_PRECEDENCE);
    }

    @Override
    protected ModelAndView doResolveException(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, Object handler, Exception ex) {
        ExceptionEnum.dealWithKnownException(request, response, ex);
        return new ModelAndView();
    }

}
