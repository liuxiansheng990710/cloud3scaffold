package com.example.redis.provider.stater.genertor;

import java.lang.reflect.Method;

import org.springframework.cache.interceptor.SimpleKeyGenerator;

import lombok.NonNull;

/**
 * <p>
 * redis中key存储按字符串进行存储
 * </p>
 *
 * @author : 21
 * @since : 2024/10/10 15:37
 */

public class ToStringKeyGenerator extends SimpleKeyGenerator {

    @Override
    @NonNull
    public Object generate(@NonNull Object target, @NonNull Method method, Object... params) {
        Object generate = super.generate(target, method, params);
        return generate.toString();
    }
}
