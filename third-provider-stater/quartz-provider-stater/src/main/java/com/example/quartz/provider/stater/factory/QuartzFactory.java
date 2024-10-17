package com.example.quartz.provider.stater.factory;

import java.util.Map;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.example.quartz.provider.stater.cons.QuartzCons;
import com.example.quartz.provider.stater.convert.QuartzJobStructConvert;
import com.example.quartz.provider.stater.enums.QuartzJobEnum;
import com.example.quartz.provider.stater.model.QuartzJob;
import com.example.quartz.provider.stater.support.IExecuteQuartzJob;
import com.example.quartz.provider.stater.support.QuartzManage;

/**
 * <p>
 * 定时任务工厂
 * </p>
 *
 * @author : 21
 * @since : 2024/10/17 11:15
 */

@Component
public class QuartzFactory implements InitializingBean {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuartzManage quartzManage;

    @Override
    public void afterPropertiesSet() {
        Map<String, IExecuteQuartzJob> beansOfType = applicationContext.getBeansOfType(IExecuteQuartzJob.class);
        if (CollectionUtils.isEmpty(beansOfType)) {
            return;
        }
        beansOfType.forEach((containerNodeBeanName, containerNodeService) -> {
            String name = containerNodeService.getClass().getName();
            QuartzJobEnum instance = QuartzJobEnum.getInstance(name);
            QuartzJob quartzJob = QuartzJobStructConvert.INSTANCE.convertQuartz(instance);
            //不存在时直接添加任务
            if (!quartzManage.exists(quartzJob.getId())) {
                quartzManage.addJob(quartzJob);
            } else {
                //存在时如果修改了cron表达式或者类 则修改该任务
                CronTrigger cronTrigger = quartzManage.getCronTrigger(quartzJob.getId());
                QuartzJob oldQuartzJob = (QuartzJob) cronTrigger.getJobDataMap().get(QuartzCons.JOB_KEY_PREFIX);
                boolean cronChange = !instance.getCron().equals(cronTrigger.getCronExpression());
                boolean classChange = !instance.getClassName().equals(oldQuartzJob.getClassName());
                if (cronChange || classChange) {
                    quartzManage.updateJobCron(quartzJob);
                }
            }
        });
    }
}
