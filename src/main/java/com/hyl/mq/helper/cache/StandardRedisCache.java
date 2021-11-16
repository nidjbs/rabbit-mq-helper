package com.hyl.mq.helper.cache;


import com.hyl.mq.helper.util.JsonUtil;
import com.hyl.mq.helper.util.RedisUtil;
import org.springframework.util.Assert;

import java.time.Duration;


/**
 * @author huayuanlin
 * @date 2021/09/18 16:16
 * @desc the class desc
 */
public abstract class StandardRedisCache<V> implements Cache<String, V> {

    /***unit：s */
    protected final int keyTimeOut;

    protected StandardRedisCache(int keyTimeOut) {
        this.keyTimeOut = keyTimeOut;
    }

    @Override
    public void initCache() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(String key) {
        Object value = RedisUtil.valueOps().get(key);
        if (value == null) {
            return null;
        }
        return isBasicTypeValue() ? (V) value : JsonUtil.fromJson((String) value, curGenericClass());
    }

    /**
     *  json class type
     *
     * @return class
     */
    protected abstract Class<V> curGenericClass();

    private boolean isBasicTypeValue() {
        Class<V> vClass = curGenericClass();
        return vClass == Integer.class || vClass == String.class || vClass == Long.class
                || vClass == Boolean.class || vClass == int.class || vClass == long.class
                || vClass == boolean.class;
    }


    @Override
    public void put(String key, V value) {
        Assert.notNull(value, "put redis cache value is null");
        Assert.hasText(key, "put redis cache key is null");
        RedisUtil.valueOps().set(key, isBasicTypeValue() ? value : JsonUtil.toJsonString(value), Duration.ofSeconds(keyTimeOut));
    }

    @Override
    public void clean() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void remove(String key) {
        Assert.hasText(key, "remove redis cache key is null");
        RedisUtil.delete(key);
    }
}
