package com.hyl.mq.helper.consumer;

import com.hyl.mq.helper.util.SpringBeanUtil;

import java.lang.reflect.ParameterizedType;

/**
 * @author huayuanlin
 * @date 2021/10/24 22:40
 * @desc the class desc
 */
public class SingleSpringBeanWrapper<T> {

    private T t;

    private Class<T> tClass;

    @SuppressWarnings("unchecked")
    private Class<T> obtainCurT() {
        if (tClass != null) {
            return tClass;
        }
        ParameterizedType ptClass = (ParameterizedType) (this.getClass().getGenericSuperclass());
        this.tClass = (Class<T>) ptClass.getActualTypeArguments()[0];
        return this.tClass;
    }

    public T getBean() {
        if (t == null) {
            synchronized (this) {
                if (t == null) {
                    t = SpringBeanUtil.getBeanByType(obtainCurT());
                }
            }
        }
        return t;
    }


}
