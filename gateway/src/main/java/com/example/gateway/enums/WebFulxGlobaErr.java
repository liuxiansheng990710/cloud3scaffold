package com.example.gateway.enums;

import org.springframework.http.HttpStatus;

import com.example.commons.core.enums.ErrorLevel;
import com.example.commons.core.model.Errors;

public enum WebFulxGlobaErr implements Errors {

    x401(HttpStatus.UNAUTHORIZED, ErrorLevel.REMIND, "您还没有登录或登录凭证已失效，请重新登录", true),
    x403(HttpStatus.FORBIDDEN, ErrorLevel.REMIND, "无权查看", true),
    x404(HttpStatus.NOT_FOUND, ErrorLevel.UNEXPECTED, "请求的路径不存在~", true),
    x504(HttpStatus.GATEWAY_TIMEOUT, ErrorLevel.CRASH, "网络错误，请重试", true),

    x2000(HttpStatus.INTERNAL_SERVER_ERROR, ErrorLevel.CRASH, "网络错误，请重试", true),
    x2002(HttpStatus.INTERNAL_SERVER_ERROR, ErrorLevel.CRASH, "网络好像出了点问题~，请稍后重试", true),

    ;

    private final HttpStatus status;
    private final ErrorLevel level;
    private final String msg;
    private final boolean show;

    WebFulxGlobaErr(HttpStatus status, ErrorLevel level, String msg, boolean show) {
        this.status = status;
        this.level = level;
        this.msg = msg;
        this.show = show;
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
