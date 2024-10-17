package com.example.commons.core.model.log;

import java.util.Date;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 定时任务日志
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 11:37
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class JobLogger extends SuperLogger {

    public static final String LOG_PREFIX = "<quartz> - ";

    /**
     * 任务id
     */
    private Long jobId;
    /**
     * Class名称
     */
    private String className;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 异常信息
     */
    private String exception;
    /**
     * 是否成功
     */
    private Boolean successed;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 参数
     */
    private JSONObject params;
    /**
     * 时间
     */
    private Date date;

}
