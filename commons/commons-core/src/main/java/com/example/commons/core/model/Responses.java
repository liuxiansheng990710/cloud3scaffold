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

    private Integer status;

    private String error;

    private String exception;

    private String msg;

    private String ranking;

    private Date time;

    private T result;

}
