package com.example.commons.web.servlet.response;

import java.util.Objects;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.commons.core.model.Responses;
import com.example.commons.web.utils.ResponseUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;

/**
 * <p>
 * 微服务接口统一返回处理
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 14:30
 */

@RestControllerAdvice("com.example.provider")
public class CommonsControllerAdvice implements ResponseBodyAdvice<Object> {

    //-------------------------------------------------------------响应---------这里只有成功才会调用----Feign成功时也会进来-------------------------------------------------------//
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class converterType) {
        //必须有两个参数中的一个 返回为true时,运行beforeBodyWrite方法
        RestController restController = returnType.getDeclaringClass().getAnnotation(RestController.class);
        ResponseBody responseBody = returnType.getMethodAnnotation(ResponseBody.class);
        return (Objects.nonNull(restController) || Objects.nonNull(responseBody));
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        HttpStatus status = HttpStatus.OK;
        servletResponse.setStatus(status.value());
        body = body instanceof Responses ? body : ResponseUtils.<Object>success(servletResponse, status, body);
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        ResponseUtils.responseAndPrint(servletRequest, servletResponse, body);
        return body;
    }

}
