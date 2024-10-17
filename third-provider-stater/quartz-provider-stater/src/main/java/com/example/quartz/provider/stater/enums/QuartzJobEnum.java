package com.example.quartz.provider.stater.enums;

import com.alibaba.fastjson2.JSONObject;

/**
 * <p>
 * 定时任务枚举，可用mysql表代替
 * </p>
 *
 * @author : 21
 * @since : 2024/10/17 15:41
 */

public enum QuartzJobEnum {

    DEFALUT(123L, "默认job", "", null, "* * * * * ? 2100", true, "备注"),
    //这个是示例 JOB_TEST(123L, "测试job", "com.example.quartz.provider.stater.support.Test", null, "0/5 * * * * ? *", false, "备注")

    ;

    /**
     * 任务id
     */
    private final Long id;
    /**
     * 任务名称
     */
    private final String taskName;
    /**
     * 任务类名（Class名称）
     */
    private final String className;
    /**
     * 任务执行参数
     */
    private final JSONObject params;
    /**
     * cron表达式
     */
    private final String cron;
    /**
     * 是否暂停
     */
    private final Boolean paused;
    /**
     * 备注
     */
    private final String description;

    QuartzJobEnum(Long id, String taskName, String className, JSONObject params, String cron, Boolean paused, String description) {
        this.id = id;
        this.taskName = taskName;
        this.className = className;
        this.params = params;
        this.cron = cron;
        this.paused = paused;
        this.description = description;
    }

    public static QuartzJobEnum getInstance(String className) {
        QuartzJobEnum[] values = QuartzJobEnum.values();
        for (QuartzJobEnum jobEnum : values) {
            if (jobEnum.className.equals(className)) {
                return jobEnum;
            }
        }
        return QuartzJobEnum.DEFALUT;
    }

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getClassName() {
        return className;
    }

    public JSONObject getParams() {
        return params;
    }

    public String getCron() {
        return cron;
    }

    public Boolean getPaused() {
        return paused;
    }

    public String getDescription() {
        return description;
    }
}
