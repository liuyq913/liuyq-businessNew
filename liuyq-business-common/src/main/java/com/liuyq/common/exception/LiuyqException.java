package com.liuyq.common.exception;

/**
 * Created by liuyq on 2017/10/30.
 */
public class LiuyqException extends BTException{
    public LiuyqException() {
    }

    public LiuyqException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiuyqException(String message) {
        super(message);
    }

    public LiuyqException(Throwable cause) {
        super(cause);
    }
}
