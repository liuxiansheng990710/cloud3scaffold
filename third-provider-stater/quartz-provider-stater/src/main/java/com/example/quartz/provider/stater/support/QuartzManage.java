package com.example.quartz.provider.stater.support;

import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.stereotype.Component;

import com.example.commons.core.exceptions.ServerException;
import com.example.quartz.provider.stater.cons.QuartzCons;
import com.example.quartz.provider.stater.model.QuartzJob;
import com.google.common.base.Throwables;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * Quartz任务管理器
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 11:17
 */

@Slf4j
@Component
public class QuartzManage {

    @Resource
    private Scheduler scheduler;

    public void addJob(QuartzJob quartzJob) {
        try {
            // 构建job信息
            JobDetail jobDetail = JobBuilder.newJob(QuartzExecutionJob.class).
                    withIdentity(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId()).build();

            //通过触发器名和cron 表达式创建 Trigger
            Trigger cronTrigger = newTrigger()
                    .withIdentity(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId())
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(quartzJob.getCron()))
                    .build();

            cronTrigger.getJobDataMap().put(QuartzCons.JOB_KEY_PREFIX, quartzJob);

            //重置启动时间
            ((CronTriggerImpl) cronTrigger).setStartTime(new Date());

            //执行定时任务
            scheduler.scheduleJob(jobDetail, cronTrigger);

            // 暂停任务
            if (quartzJob.getPaused()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("Failed to create scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to create scheduled task", e);
        }
    }

    /**
     * 检测该任务是否存在
     *
     * @param jobId
     * @return
     */
    public boolean exists(Long jobId) {
        try {
            return scheduler.checkExists(new JobKey(QuartzCons.JOB_NAME_PREFIX + jobId));
        } catch (SchedulerException e) {
            log.error("Failed to exists scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to exists scheduled task", e);
        }
    }

    /**
     * 获取所有触发器
     *
     * @return
     */
    public Set<TriggerKey> triggerKeys() {
        try {
            return scheduler.getTriggerKeys(GroupMatcher.anyGroup());
        } catch (SchedulerException e) {
            log.error("Failed to get trigger keys: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to get trigger keys", e);
        }
    }

    /**
     * 获取触发器
     *
     * @param jobId
     * @return
     */
    public CronTrigger getCronTrigger(Long jobId) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzCons.JOB_NAME_PREFIX + jobId);
            return (CronTrigger) scheduler.getTrigger(triggerKey);
        } catch (SchedulerException e) {
            log.error("Failed to get cron trigger: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to get cron trigger", e);
        }
    }

    /**
     * 更新job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void updateJobCron(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
                trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            }
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(quartzJob.getCron());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            //重置启动时间
            ((CronTriggerImpl) trigger).setStartTime(new Date());
            trigger.getJobDataMap().put(QuartzCons.JOB_KEY_PREFIX, quartzJob);

            scheduler.rescheduleJob(triggerKey, trigger);
            // 暂停任务
            if (quartzJob.getPaused()) {
                pauseJob(quartzJob);
            }
        } catch (Exception e) {
            log.error("Failed to update scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to update scheduled task", e);
        }

    }

    /**
     * 删除一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void deleteJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            scheduler.deleteJob(jobKey);
        } catch (Exception e) {
            log.error("Failed to delete scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to delete scheduled task", e);
        }
    }

    /**
     * 批量删除job
     *
     * @param quartzJobs
     * @throws SchedulerException
     */
    public void deleteJobs(List<QuartzJob> quartzJobs) {
        try {
            List<JobKey> jobKeys = quartzJobs.stream().map(quartzJob -> JobKey.jobKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId())).collect(Collectors.toList());
            scheduler.deleteJobs(jobKeys);
        } catch (Exception e) {
            log.error("Failed to delete scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to delete scheduled task", e);
        }
    }

    /**
     * 恢复一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void resumeJob(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobKey jobKey = JobKey.jobKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            scheduler.resumeJob(jobKey);
        } catch (Exception e) {
            log.error("Failed to recovery scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to recovery scheduled task", e);
        }
    }

    /**
     * 立即执行job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void runAJobNow(QuartzJob quartzJob) {
        try {
            TriggerKey triggerKey = TriggerKey.triggerKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            // 如果不存在则创建一个定时任务
            if (trigger == null) {
                addJob(quartzJob);
            }
            JobDataMap dataMap = new JobDataMap();
            dataMap.put(QuartzCons.JOB_KEY_PREFIX, quartzJob);
            JobKey jobKey = JobKey.jobKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            scheduler.triggerJob(jobKey, dataMap);
        } catch (Exception e) {
            log.error("Failed to execution scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to execution scheduled task", e);
        }
    }

    /**
     * 暂停一个job
     *
     * @param quartzJob
     * @throws SchedulerException
     */
    public void pauseJob(QuartzJob quartzJob) {
        try {
            JobKey jobKey = JobKey.jobKey(QuartzCons.JOB_NAME_PREFIX + quartzJob.getId());
            scheduler.pauseJob(jobKey);
        } catch (Exception e) {
            log.error("Failed to pause scheduled task: {}", Throwables.getStackTraceAsString(e));
            throw new ServerException("Failed to pause scheduled task", e);
        }
    }
}

