package com.example.redis.provider.stater.lock.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 锁参数注解
 * <P>1. 可代替Klock#keys 直接在参数前加注解</P>
 * <P>2. 自定义锁key参数，不用受限于参数值</P>
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:16
 */

@Target(value = {ElementType.PARAMETER, ElementType.TYPE})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface KlockKey {

    String value() default "";

}
