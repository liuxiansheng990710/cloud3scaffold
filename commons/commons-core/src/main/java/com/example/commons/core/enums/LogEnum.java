package com.example.commons.core.enums;

/**
 * <p>
 * 日志类型枚举类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 10:05
 */

public enum LogEnum {

    REQUEST("request", 1, "请求日志"),
    MONGO("mongo", 2, "mongodb日志"),
    MYSQL("mysql", 3, "mysql日志"),
    RABBIT_MQ("rabbit_mq", 4, "rabbit_mq运行日志"),

    ;

    private final String desc;

    private final Integer type;

    private final String summary;

    LogEnum(String desc, Integer type, String summary) {
        this.desc = desc;
        this.type = type;
        this.summary = summary;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getType() {
        return type;
    }

    public String getSummary() {
        return summary;
    }
}
