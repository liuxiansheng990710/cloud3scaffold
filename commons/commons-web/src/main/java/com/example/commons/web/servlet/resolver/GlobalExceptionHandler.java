package com.example.commons.web.servlet.resolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * 全局异常处理器
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 14:42
 */

public interface GlobalExceptionHandler {

    void handle(HttpServletRequest request, HttpServletResponse response, Exception ex);

}
