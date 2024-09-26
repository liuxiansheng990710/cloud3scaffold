package com.example.commons.web.servlet.enums;

import org.springframework.http.HttpStatus;

import com.example.commons.core.enums.ErrorLevel;
import com.example.commons.core.model.Errors;

/**
 * <p>
 * web异常Err
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 14:48
 */

public enum WebGlobaErr implements Errors {

    x404(HttpStatus.NOT_FOUND, ErrorLevel.UNEXPECTED, "未找到"),
    x405(HttpStatus.METHOD_NOT_ALLOWED, ErrorLevel.UNEXPECTED, "请求方式不支持"),
    x406(HttpStatus.NOT_ACCEPTABLE, ErrorLevel.UNEXPECTED, "无法生成该请求的媒体类型"),
    x415(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ErrorLevel.UNEXPECTED, "不支持的请求媒体类型"),
    x504(HttpStatus.GATEWAY_TIMEOUT, ErrorLevel.CRASH, "网络错误，请重试"),
    x510(HttpStatus.REQUEST_TIMEOUT, ErrorLevel.CRASH, "处理异步请求超时"),

    x2000(HttpStatus.INTERNAL_SERVER_ERROR, ErrorLevel.CRASH, "网络错误，请重试"),
    x2001(HttpStatus.BAD_REQUEST, ErrorLevel.REMIND, "用户名或密码错误"),
    x2002(HttpStatus.INTERNAL_SERVER_ERROR, ErrorLevel.CRASH, "网络好像出了点问题~，请稍后重试"),

    /**
     * 接口请求参数错误
     */
    x2011(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "参数错误"),
    x2012(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "参数请求格式错误"),
    x2013(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "参数校验失败，请检查参数"),
    x2014(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "路径参数错误"),
    x2015(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "请求参数缺失"),
    x2016(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "参数类型错误"),
    x2017(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "Multipart请求参数错误"),
    x2018(HttpStatus.BAD_REQUEST, ErrorLevel.UNEXPECTED, "参数错误：Json解析出错"),
    x2019(HttpStatus.NOT_ACCEPTABLE, ErrorLevel.REMIND, "客户端提前断开连接"),

    ;

    private final HttpStatus status;
    private final ErrorLevel level;
    private final String msg;

    WebGlobaErr(HttpStatus status, ErrorLevel level, String msg) {
        this.status = status;
        this.level = level;
        this.msg = msg;
    }

    @Override
    public String getError() {
        return name();
    }

    @Override
    public int getStatus() {
        return status.value();
    }

    @Override
    public String getRanking() {
        return level.name();
    }

    @Override
    public String getMsg() {
        return msg;
    }

}
