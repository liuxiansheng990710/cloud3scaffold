package com.example.commons.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.NonNull;

/**
 * <p>
 * 获取Spring上下文 作为配置类使用
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 10:45
 */

@Component
@Lazy(false)
public class ApplicationContextRegister implements ApplicationContextAware {

    private static ApplicationContext APPLICATION_CONTEXT;

    public static ApplicationContext getApplicationContext() {
        return APPLICATION_CONTEXT;
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }
}
