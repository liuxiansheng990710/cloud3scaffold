package com.example.quartz.provider.stater.support;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.alibaba.fastjson2.JSONObject;
import com.example.commons.core.model.log.JobLogger;
import com.example.commons.core.utils.ThreadUtils;
import com.example.quartz.provider.stater.cons.QuartzCons;
import com.example.quartz.provider.stater.model.QuartzJob;
import com.google.common.base.Throwables;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 定时任务执行器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 11:18
 */

@Async
@Slf4j
public class QuartzExecutionJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) {
        QuartzJob quartzJob = (QuartzJob) context.getMergedJobDataMap().get(QuartzCons.JOB_KEY_PREFIX);
        JSONObject params = quartzJob.getParams();
        JobLogger jobLog = new JobLogger();
        jobLog.setTaskName(quartzJob.getTaskName());
        jobLog.setClassName(quartzJob.getClassName());
        jobLog.setParams(params);
        jobLog.setCron(quartzJob.getCron());
        jobLog.setJobId(quartzJob.getId());
        long startTime = System.currentTimeMillis();
        try {
            //执行任务
            QuartzRunnable task = new QuartzRunnable(quartzJob.getClassName(), quartzJob.getId(), params);
            ThreadUtils.execute(task);
            String runTime = System.currentTimeMillis() - startTime + "ms";
            jobLog.setRunTime(runTime);
            jobLog.setSuccessed(true);
        } catch (Exception e) {
            log.error("Failed to execution scheduled task, jobId {}, name {}, params {}, exception {}", quartzJob.getId(), quartzJob.getTaskName(), params, Throwables.getStackTraceAsString(e));
            jobLog.setRunTime(System.currentTimeMillis() - startTime + "ms");
            jobLog.setSuccessed(false);
            jobLog.setException(Throwables.getStackTraceAsString(e));
        } finally {
            jobLog.setDate(new Date());
            // TODO 定时任务日志 这里可以存库，目前只打印一下
            log.info(JobLogger.LOG_PREFIX + jobLog);
        }
    }
}
