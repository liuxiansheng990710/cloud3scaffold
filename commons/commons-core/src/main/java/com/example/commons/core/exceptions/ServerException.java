package com.example.commons.core.exceptions;

import java.io.Serial;

/**
 * <p>
 * 服务运行异常
 * </p>
 *
 * @author : 21
 * @since : 2024/9/26 14:52
 */

public class ServerException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public ServerException(String message) {
        super(message);
    }

    public ServerException(Throwable throwable) {
        super(throwable);
    }

    public ServerException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
