package com.hyl.mq.helper.exx;

/**
 * @author huayuanlin
 * @date 2021/06/17 14:23
 * @desc the class desc
 */
public class FrameworkException extends RuntimeException {

    public FrameworkException() {
        super();
    }

    public FrameworkException(String message) {
        super(message);
    }

    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    public FrameworkException(Throwable cause) {
        super(cause);
    }

    protected FrameworkException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
