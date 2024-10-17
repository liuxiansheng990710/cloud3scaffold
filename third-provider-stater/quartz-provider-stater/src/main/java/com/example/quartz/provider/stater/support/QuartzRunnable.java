package com.example.quartz.provider.stater.support;

import java.lang.reflect.Method;

import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson2.JSONObject;
import com.example.commons.core.exceptions.CommonUtilsException;
import com.example.commons.core.exceptions.ServerException;
import com.example.commons.core.utils.ApplicationContextRegister;

/**
 * <p>
 * 定时异步任务执行方法
 * </p>
 *
 * @author : 21
 * @since : 2024/10/15 11:28
 */

public class QuartzRunnable implements Runnable {

    //任务执行目标
    private final Object target;
    private final Method method;
    private final Long jobId;
    private final JSONObject params;

    QuartzRunnable(String className, Long jobId, JSONObject params) throws NoSuchMethodException {
        //任务执行的类
        this.target = ApplicationContextRegister.getApplicationContext().getBean(forName(className));
        //获取excute方法（具体执行方法）
        this.method = target.getClass().getDeclaredMethod("execute", Long.class, JSONObject.class);
        this.jobId = jobId;
        this.params = params;
    }

    @Override
    public void run() {
        try {
            //将该方法访问修饰符设置为pubilc（也就是可访问）
            ReflectionUtils.makeAccessible(method);
            method.invoke(target, jobId, params);
        } catch (Exception e) {
            throw new ServerException("Failed to execution scheduled task", e);
        }
    }

    /**
     * 根据类名获取类
     *
     * @param className
     * @return
     */
    private Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new CommonUtilsException("Error: forName Exception: String class [" + className + "]", e);
        }
    }
}
