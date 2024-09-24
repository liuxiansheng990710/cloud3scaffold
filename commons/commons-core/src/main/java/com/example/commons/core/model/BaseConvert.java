package com.example.commons.core.model;

import java.io.Serial;
import java.io.Serializable;

import com.example.commons.core.utils.BeanConvert;

/**
 * <p>
 * 基础实体父类（提供实体转换方法）
 * </p>
 *
 * @author : 21
 * @since : 2024/9/24 16:29
 */

public class BaseConvert implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 获取自动转换后的JavaBean对象
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T convert(Class<T> clazz) {
        return BeanConvert.convert(this, clazz);
    }

}
