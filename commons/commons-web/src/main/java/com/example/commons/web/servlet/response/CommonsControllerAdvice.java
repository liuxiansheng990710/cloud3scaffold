package com.example.commons.web.servlet.response;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.commons.core.model.Responses;
import com.example.commons.core.cons.RequestCons;
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
public class CommonsControllerAdvice implements ResponseBodyAdvice<Object>, RequestBodyAdvice {

    //-------------------------------------------------------------响应---------这里只有成功才会调用----Feign成功时也会进来-------------------------------------------------------//
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class converterType) {
        //必须有两个参数中的一个 返回为true时,运行beforeBodyWrite方法
        RestController restController = returnType.getDeclaringClass().getAnnotation(RestController.class);
        ResponseBody responseBody = returnType.getMethodAnnotation(ResponseBody.class);
        return (Objects.nonNull(restController) || Objects.nonNull(responseBody));
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        Object object = body;
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();

        Wrapper annotation = returnType.getMethodAnnotation(Wrapper.class);
        HttpStatus status = Objects.isNull(annotation) ? HttpStatus.OK : annotation.httpStatus();

        if (Objects.isNull(annotation) || annotation.wrapper()) {
            object = body instanceof Responses ? body : ResponseUtils.<Object>success(servletResponse, status, body);
        }
        ResponseUtils.responseAndPrint(servletRequest, servletResponse, object);
        return object;
    }

    //-------------------------------------------------------请求----------- 这里只对@RequestBody注解的参数起作用-----------------------------------------------------------//

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        //不校验,直接走到beforeBodyRead方法
        return true;
    }

    @Override
    @NonNull
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter,
                                           @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        //读取请求体之前,暂不修改请求
        return inputMessage;
    }

    @Override
    @NonNull
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter,
                                @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        //将请求体存储使用
        Optional<HttpServletRequest> optionalRequest = Optional.of(((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest());
        optionalRequest.ifPresent(request -> request.setAttribute(RequestCons.REQ_BODY, body));
        return body;
    }

    @Override
    @NonNull
    public Object handleEmptyBody(Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter,
                                  @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        //请求体为空时,直接返回
        return body;
    }
}
