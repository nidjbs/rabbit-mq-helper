package com.hyl.mq.helper.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * @author huayuanlin
 * @date 2021/08/04 10:58
 * @desc the class desc
 */
public class SafeExecuteUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(SafeExecuteUtil.class);

    private SafeExecuteUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Safe execution logic that catches specified exceptions (including subclass exceptions)
     *
     * @param logicCallBack logic
     * @param catchExe      specified exceptions(including subclass exceptions)
     */
    public static void safeExecute(SimpleCallBack logicCallBack, Class<? extends Throwable> catchExe) {
        safeExecute(logicCallBack, null, catchExe);
    }

    /**
     * Safe execution logic that catches specified exceptions (including subclass exceptions)
     *
     * @param logicCallBack logic
     * @param failCallBack  when execute fail,execute the failCallBack logic
     * @param catchExe      specified exceptions(including subclass exceptions)
     */
    public static void safeExecute(SimpleCallBack logicCallBack, SimpleCallBack failCallBack,
                                   Class<? extends Throwable> catchExe) {
        Assert.notNull(logicCallBack, "call back logic");
        Assert.notNull(catchExe, "catchExe class");
        try {
            logicCallBack.callBack();
        } catch (Throwable e) {
            if (failCallBack != null) {
                failCallBack.callBack();
            }
            boolean assignable = catchExe.isAssignableFrom(e.getClass());
            if (!assignable) {
                throw e;
            } else {
                LOGGER.warn("do safe execute fail,this exception is catch!", e);
            }
        }
    }


    public interface SimpleCallBack {

        /**
         * simple call back
         */
        void callBack();
    }

}
