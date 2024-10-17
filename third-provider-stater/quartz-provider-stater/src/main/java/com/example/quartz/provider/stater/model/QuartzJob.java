package com.example.quartz.provider.stater.model;

import java.io.Serializable;

import com.alibaba.fastjson2.JSONObject;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 定时任务实体类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 11:12
 */

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class QuartzJob implements Serializable {

    /**
     * 任务id
     */
    private Long id;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 任务类名（Class名称）
     */
    private String className;
    /**
     * 任务执行参数
     */
    private JSONObject params;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * 是否暂停
     */
    private Boolean paused;
    /**
     * 备注
     */
    private String description;

}
