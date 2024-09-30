package com.example.commons.core.model.log;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * mongo日志类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 10:12
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MongoLog extends SuperLogger {

    public static final String LOG_PREFIX = "<mongo> - ";

    /**
     * sql
     */
    private String sql;

    /**
     * 集合
     */
    private String collection;

    /**
     * 数据库
     */
    private String dataBase;

    /**
     * sql类型
     */
    private String sqlType;

    /**
     * 是否成功
     */
    private boolean success;

}
