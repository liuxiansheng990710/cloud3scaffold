package com.example.commons.core.model.log;

import java.util.Date;
import java.util.Map;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 请求日志
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 11:07
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Slf4j
public class RequestLogger extends SuperLogger {

    public static final String LOG_PREFIX = "<log> - ";

    /**
     * 请求开始时间
     */
    private Date startTime;
    /**
     * 请求参数
     */
    private Map parameterMap;
    /**
     * 请求体
     */
    private Object requestBody;
    /**
     * 请求路径
     */
    private String url;
    /**
     * 请求mapping
     */
    private String mapping;
    /**
     * 请求方法
     */
    private String method;
    /**
     * 返回结果（成功时正常返回，失败时带异常信息）
     */
    private Object data;
    /**
     * IP地址
     */
    private String ip;

    /**
     * @param status
     * @param logger
     */
    public static void print(int status, String logger) {
        if (status >= 500) {
            log.error(logger);
            return;
        }
        log.info(logger);
    }

}
