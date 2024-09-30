package com.example.commons.core.model.log;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * sql日志
 * </p>
 *
 * @author : 21
 * @since : 2024/9/30 10:49
 */

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SQLLogger extends SuperLogger {

    public static final String LOG_PREFIX = "<sql> - ";

    /**
     * SQL
     */
    private String sql;

}
