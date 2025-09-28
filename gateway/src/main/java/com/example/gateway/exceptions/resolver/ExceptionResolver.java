package com.example.gateway.exceptions.resolver;

import java.lang.reflect.UndeclaredThrowableException;
import java.net.URI;
import java.util.Date;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.example.commons.core.exceptions.ApiException;
import com.example.commons.core.exceptions.ApiFeignClientException;
import com.example.commons.core.model.ErrorCode;
import com.example.commons.core.model.Errors;
import com.example.commons.core.model.Responses;
import com.example.commons.core.utils.JacksonUtils;
import com.example.gateway.enums.WebFulxGlobaErr;
import com.google.common.base.Throwables;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * webflux错误信息解析
 * <p>
 *
 * @author : 21
 * @since : 2024/1/19 10:28
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResolver {

    /**
     * 返回错误信息并打印错误日志
     *
     * @param exchange
     * @param throwable
     * @return
     */
    public static Responses<?> failedResponse(ServerWebExchange exchange, Throwable throwable) {
        return failedResponse(exchange, getErrors(throwable), throwable);
    }

    /**
     * 返回错误信息并打印错误日志
     *
     * @param exchange
     * @param errors
     * @param throwable
     * @return
     */
    public static Responses<?> failedResponse(ServerWebExchange exchange, Errors errors, Throwable throwable) {
        exchange.getResponse().setRawStatusCode(errors.getStatus());
        Responses<?> failedResponse = new Responses<>();
        failedResponse.setError(errors.getError())
                .setMsg(errors.getMsg())
                .setRanking(errors.getRanking())
                .setStatus(errors.getStatus())
                .setRequestId(MDC.get("traceId"))
                .setTime(new Date());
        if (!(throwable instanceof ApiException)) {
            failedResponse.setException(StringUtils.substring(Throwables.getStackTraceAsString(throwable), 0, 4096));
        }
        failedResponse.setException(null);
        //打印错误日志
//        RequestLogger logger = RequestLogger.getRequestLogger(exchange, failedResponse, getForwardHosts(exchange));
//        RequestLogger.errorPrint(errors.getStatus(), RequestLogger.LOG_PREFIX + logger);
        return failedResponse;
    }

    /**
     * 获取转发hosts
     *
     * @param exchange
     * @return
     */
    private static String getForwardHosts(ServerWebExchange exchange) {
        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
        return Objects.nonNull(uri) ? uri.getAuthority() : null;
    }

    /**
     * 根据异常类型 获取错误对象
     *
     * @param ex
     * @return
     */
    private static Errors getErrors(Throwable ex) {
        if (ex instanceof ApiException exception) {
            return exception.getErrors();
        }
        else if (ex instanceof UndeclaredThrowableException &&
                Objects.nonNull(ex.getCause().getCause()) &&
                ex.getCause().getCause() instanceof ApiFeignClientException exception) {
            Errors errors = JacksonUtils.readValue(exception.getErrerBodyMsg(), ErrorCode.class);
            return Objects.nonNull(errors) ? errors : WebFulxGlobaErr.x2002;
        }
        else {
            final int httpStatus;
            if (ex instanceof ResponseStatusException exception) {
                httpStatus = exception.getStatusCode().value();
            } else {
                httpStatus = HttpStatus.INTERNAL_SERVER_ERROR.value();
            }
            return switch (httpStatus) {
                // 404 没有找到对应的页面、接口
                case org.apache.http.HttpStatus.SC_NOT_FOUND -> WebFulxGlobaErr.x404;
                // 503 就是服务还没注册上
                case org.apache.http.HttpStatus.SC_SERVICE_UNAVAILABLE -> WebFulxGlobaErr.x2000;
                // 504 服务响应超时
                case org.apache.http.HttpStatus.SC_GATEWAY_TIMEOUT -> WebFulxGlobaErr.x504;
                // 默认 500 系统错误
                default -> WebFulxGlobaErr.x2002;
            };
        }
    }

}
