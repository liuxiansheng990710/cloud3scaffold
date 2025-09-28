package com.example.gateway.utils;

import java.util.Date;

import org.slf4j.MDC;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.example.commons.core.exceptions.ApiException;
import com.example.commons.core.model.Errors;
import com.example.commons.core.model.Responses;
import com.example.commons.core.utils.JacksonUtils;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * IP地址获取工具类
 *
 * @author Caratacus
 */
@Slf4j
public abstract class WebFluxResponseUtils {

//    /**
//     * 获取日志对象
//     *
//     * @param exchange
//     * @param failedResponse
//     * @return
//     */
//    public static RequestLogger getRequestLogger(ServerWebExchange exchange, Responses failedResponse) {
//        ServerHttpRequest request = exchange.getRequest();
//        HttpHeaders httpHeaders = request.getHeaders();
//        Long beiginTime = TypeUtils.castToLong(httpHeaders.getFirst(RequestCons.REQ_BEGIN_TIME));
//        // 调用接口结束时间
//        Long endTime = System.currentTimeMillis();
//        RequestLogger logger = new RequestLogger();
//        logger.setParameterMap(request.getQueryParams())
//                .setUrl(request.getPath().value())
//        //请求mapping
//        logger.setMapping(httpHeaders.getFirst(AppCons.APP_MAPPING));
//        //请求方法
//        logger.setMethod(request.getMethodValue());
//        logger.setRunTime((beiginTime != null ? endTime - beiginTime : 0) + "ms");
//        logger.setHeaders(headers);
//        logger.setData(failedResponse);
//        logger.setRequestBody(resolveRequestBody(exchange));
//        logger.setMark(MarkEnum.NORMAL.name());
//        String ipAddress = httpHeaders.getFirst(AppCons.APP_X_REAL_IP);
//        if (StringUtils.isBlank(ipAddress)) {
//            ipAddress = GatewayIpUtils.getIpAddr(httpHeaders, request);
//        }
//        logger.setIp(ipAddress);
//        logger.setPayload(payloadObject);
//        logger.setInlet(Boolean.TRUE.toString());
//        URI uri = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);
//        if (Objects.nonNull(uri)) {
//            String authority = uri.getAuthority();
//            logger.setForwardHosts(authority);
//        }
//        return logger;
//    }
//
//
//    /**
//     * 读取请求体内容
//     *
//     * @param exchange
//     * @return
//     */
//    private static Object resolveRequestBody(ServerWebExchange exchange) {
//        byte[] requestBody = exchange.getAttribute(ReadBodyFactory.CACHE_REQUEST_BODY_OBJECT_KEY);
//        return Optional.ofNullable(JacksonUtils.readValue(requestBody, Object.class)).orElse(requestBody);
//    }

    /**
     * 根据ErrorCode获取响应对象
     *
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
            failedResponse.setException(org.apache.commons.lang3.StringUtils.substring(Throwables.getStackTraceAsString(throwable), 0, 4096));
        }
//        RequestLogger logger = WebFluxResponseUtils.getRequestLogger(exchange, failedResponse);
//        if (errors.getStatus() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
//            log.error(RequestLogger.LOG_PREFIX + logger);
//        } else {
//            log.warn(RequestLogger.LOG_PREFIX + logger);
//        }
        failedResponse.setException(null);
        failedResponse.setTime(null);
        return failedResponse;
    }

    /**
     * 获取异常响应byte对象
     *
     * @param errors
     * @param exchange
     * @param throwable
     * @return
     */
    public static byte[] getFailureResponseBytes(ServerWebExchange exchange, Errors errors, Throwable throwable) {
        Responses<?> failedResponse = WebFluxResponseUtils.failedResponse(exchange, errors, throwable);
        return JacksonUtils.toBytes(failedResponse);
    }

    /**
     * 获取DataBuffer
     *
     * @param response
     * @param bytes
     * @return
     */
    public static DataBuffer getDataBuffer(ServerHttpResponse response, byte[] bytes) {
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Cache-Control", "no-cache");
        return response.bufferFactory().wrap(bytes);
    }

    /**
     * 捕捉异常向客户端输出异常信息
     *
     * @param errors
     * @param exchange
     * @param throwable
     * @return
     */
    public static Mono<Void> writeWithThrowable(Errors errors, ServerWebExchange exchange, Throwable throwable) {
        return Mono.defer(
                () -> Mono.just(exchange.getResponse())
        ).flatMap(response -> {
            byte[] bytes = WebFluxResponseUtils.getFailureResponseBytes(exchange, errors, throwable);
            DataBuffer buffer = WebFluxResponseUtils.getDataBuffer(response, bytes);
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        });
    }

}
