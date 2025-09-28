package com.example.commons.core.exceptions;


/**
 * <p>
 * Feign异常类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/30 17:50
*/


public class ApiFeignClientException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int status;

    private final String errerBodyMsg;

    public ApiFeignClientException(String errerBodyMsg) {
        super(errerBodyMsg);
        this.errerBodyMsg = errerBodyMsg;
    }

    public ApiFeignClientException(int status, String errerBodyMsg) {
        this(errerBodyMsg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getErrerBodyMsg() {
        return errerBodyMsg;
    }

}
