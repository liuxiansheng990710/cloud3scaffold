package com.example.quartz.provider.stater.support;

import com.alibaba.fastjson2.JSONObject;

/**
 * <p>
 * 定时任务执行接口
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 14:39
 */

public interface IExecuteQuartzJob {

    /**
     * 执行定时任务
     *
     * @param jobId
     * @param parm
     */
    void execute(Long jobId, JSONObject parm);

}
