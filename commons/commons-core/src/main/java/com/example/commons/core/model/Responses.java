package com.example.commons.core.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公共返回体
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 10:51
 */

@Getter
@Setter
@Accessors(chain = true)
public class Responses<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * http状态码
     */
    private Integer status;

    /**
     * 自定义错误状态码
     */
    private String error;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 异常提示
     */
    private String msg;

    /**
     * 自定义描述
     */
    private String desc;

    /**
     * 异常等级
     */
    private String ranking;

    /**
     * 时间
     */
    private Date time;

    /**
     * 结果
     */
    private T result;

    /**
     * 请求id
     */
    private String requestId;

}
