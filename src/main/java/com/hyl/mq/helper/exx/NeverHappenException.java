package com.hyl.mq.helper.exx;

/**
 * @author huayuanlin
 * @date 2021/06/17 14:23
 * @desc the class desc
 */
public class NeverHappenException extends RuntimeException {

    public NeverHappenException() {
        super();
    }

    public NeverHappenException(String message) {
        super(message);
    }

    public NeverHappenException(String message, Throwable cause) {
        super(message, cause);
    }

    public NeverHappenException(Throwable cause) {
        super(cause);
    }

    protected NeverHappenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
