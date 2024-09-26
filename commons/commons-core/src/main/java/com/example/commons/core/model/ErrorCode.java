package com.example.commons.core.model;

import java.io.Serial;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * <p>
 * 统一异常
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 10:57
 */

@Getter
@ToString
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCode implements Errors, Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 自定义错误状态码
     */
    private String error;
    /**
     * http状态码
     */
    private int status;
    /**
     * 等级
     */
    private String ranking;
    /**
     * 错误消息
     */
    private String msg;

}
