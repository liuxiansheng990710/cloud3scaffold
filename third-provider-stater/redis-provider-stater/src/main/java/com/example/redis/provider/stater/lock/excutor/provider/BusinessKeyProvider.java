package com.example.redis.provider.stater.lock.excutor.provider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.example.redis.provider.stater.lock.annotations.Klock;
import com.example.redis.provider.stater.lock.annotations.KlockKey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 锁名称提供类（主要用于锁粒度控制）
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:26
 */

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessKeyProvider {

    private static final SpelExpressionParser parser = new SpelExpressionParser();

    private static final String LOCK_NAME_PREFIX = "redissonlock";
    private static final String LOCK_NAME_SEPARATOR = "_";

    /**
     * 获取锁名称
     * <P>举例：name = "myKey", keys = {"#a", "#b", "#c"} -> return：redissonlock_mykey_a_b_c</P>
     *
     * @param joinPoint
     * @param klock
     * @return
     */
    public static String getKeyName(JoinPoint joinPoint, Klock klock) {
        Method method = getMethod(joinPoint);
        String annotationName = getAnnotationName(klock.name(), (MethodSignature) joinPoint.getSignature());
        List<String> parameterKeys = getParameterKey(method.getParameters(), joinPoint.getArgs());
        String bussinessKeyName = StringUtils.collectionToDelimitedString(parameterKeys, "", LOCK_NAME_SEPARATOR, "");
        return LOCK_NAME_PREFIX + LOCK_NAME_SEPARATOR + annotationName + bussinessKeyName;
    }

    /**
     * 获取切面方法
     * <P>如果是接口则获取实现类方法</P>
     *
     * @param joinPoint
     * @return
     */
    public static Method getMethod(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //如果作用在接口上 寻找其实现类（寻找同名的方法）
        if (method.getDeclaringClass().isInterface()) {
            try {
                method = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                        method.getParameterTypes());
            } catch (NoSuchMethodException e) {
                log.error("该接口无实现类！ 错误信息:{}" + e.getMessage());
            }
        }
        return method;
    }

    /**
     * 获取锁name字段，如果未指定，返回全类名.方法名
     * <P>举例：com.example.MyClass#myMethod -> return：com.example.MyClass.myMethod</P>
     *
     * @param annotationName
     * @param signature
     * @return
     */
    private static String getAnnotationName(String annotationName, MethodSignature signature) {
        return annotationName.isBlank() ? String.format("%s.%s", signature.getDeclaringTypeName(), signature.getMethod().getName()) : annotationName;
    }

    /**
     * 解析KlockKey字段
     *
     * @param parameters
     * @param parameterValues
     * @return
     */
    private static List<String> getParameterKey(Parameter[] parameters, Object[] parameterValues) {
        List<String> parameterKey = new ArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getAnnotation(KlockKey.class) != null) {
                KlockKey keyAnnotation = parameters[i].getAnnotation(KlockKey.class);
                if (keyAnnotation.value().isEmpty()) {
                    Object parameterValue = parameterValues[i];
                    parameterKey.add(ObjectUtils.nullSafeToString(parameterValue));
                } else {
                    StandardEvaluationContext context = new StandardEvaluationContext(parameterValues[i]);
                    Object key = parser.parseExpression(keyAnnotation.value()).getValue(context);
                    parameterKey.add(ObjectUtils.nullSafeToString(key));
                }
            }
        }
        return parameterKey;
    }

}
