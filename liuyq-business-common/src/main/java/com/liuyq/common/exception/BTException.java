package com.liuyq.common.exception;

/**
 * Created by liuyq on 2017/10/30.
 */
public class BTException extends RuntimeException {
    public BTException() {
    }

    public BTException(String message, Throwable cause) {
        super(message, cause);
    }

    public BTException(String message) {
        super(message);
    }

    public BTException(Throwable cause) {
        super(cause);
    }
}
