package com.example.commons.core.log.models;

import java.io.Serializable;

import com.example.commons.core.utils.JacksonUtils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 日志父类
 * <p>
 *
 * @author : 21
 * @since : 2023/10/8 17:35
 */

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SuperLogger implements Serializable {

    /**
     * 来源
     */
    private String origin;
    /**
     * 环境
     */
    private String environment;

    @Override
    public String toString() {
        return JacksonUtils.toJson(this);
    }

}
