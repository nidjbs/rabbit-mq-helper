package com.hyl.mq.helper.exx;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:26
 * @desc the class desc
 */
public class UnexpectedException extends RuntimeException {

    public UnexpectedException() {
        super();
    }

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedException(Throwable cause) {
        super(cause);
    }

    protected UnexpectedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
