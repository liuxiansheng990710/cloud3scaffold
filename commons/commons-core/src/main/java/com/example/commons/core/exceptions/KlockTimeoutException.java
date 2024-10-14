package com.example.commons.core.exceptions;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 加锁超时异常
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:19
 */

public class KlockTimeoutException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private String errorMsg;

    public KlockTimeoutException() {
    }

    public KlockTimeoutException(String message, String errorMsg) {
        super(message);
        setErrorMsg(errorMsg);
    }

    public KlockTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

}
