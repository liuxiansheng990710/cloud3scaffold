package com.example.commons.core.exceptions;

import java.io.Serial;

/**
 * <p>
 * 锁调用异常
 * </p>
 *
 * @author : 21
 * @since : 2024/10/12 10:18
 */

public class KlockInvocationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public KlockInvocationException() {
    }

    public KlockInvocationException(String message) {
        super(message);
    }

    public KlockInvocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
