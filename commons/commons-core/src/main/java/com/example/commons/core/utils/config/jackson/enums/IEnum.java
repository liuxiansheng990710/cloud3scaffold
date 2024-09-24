package com.example.commons.core.utils.config.jackson.enums;

import java.util.Objects;

import com.example.commons.core.utils.config.jackson.deserializer.IEnumDeserializer;
import com.example.commons.core.utils.config.jackson.serializer.IEnumSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * <p>
 * 通用枚举类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/24 15:49
 */

@JsonSerialize(using = IEnumSerializer.class)
@JsonDeserialize(using = IEnumDeserializer.class)
public interface IEnum {

    /**
     * 获取枚举值
     *
     * @return
     */
    int getValue();

    /**
     * 获取枚举对象
     *
     * @param clazz 实现IEnum的类
     * @param value 枚举值
     * @param <T>
     * @return
     */
    static <T extends IEnum> T getInstance(Class<T> clazz, int value) {
        //如果clazz是枚举类，该方法会获取到所有枚举常量的数组  否则返回null
        T[] enumConstants = clazz.getEnumConstants();
        if (Objects.isNull(enumConstants)) {
            return null;
        }
        for (T t : enumConstants) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return null;
    }

}
