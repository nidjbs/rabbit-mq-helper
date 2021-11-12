package com.hyl.mq.helper.exx;

/**
 * @author huayuanlin
 * @date 2021/10/24 15:54
 * @desc the class desc
 */
public class ConsumerException extends RuntimeException {

    public ConsumerException() {
        super();
    }

    public ConsumerException(String message) {
        super(message);
    }

    public ConsumerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConsumerException(Throwable cause) {
        super(cause);
    }

    protected ConsumerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}