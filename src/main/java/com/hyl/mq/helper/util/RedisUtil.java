package com.hyl.mq.helper.util;

import org.springframework.data.redis.core.*;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author huayuanlin
 * @date 2021/07/16 14:00
 * @desc the class desc
 */
public class RedisUtil {

    private RedisUtil() {
        throw new UnsupportedOperationException();
    }


    private static RedisTemplate<String, Object> redisTemplate;

    /**
     * 初始化redis template
     *
     * @param redisTemplate template
     */
    public static void initRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtil.redisTemplate = redisTemplate;
    }

    /**
     * 获取redisTemplate
     * redisTemplate为空时从spring中获取
     *
     * @return redisTemplate
     */
    public static RedisTemplate<String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            synchronized (RedisUtil.class) {
                if (redisTemplate == null) {
                    redisTemplate = SpringBeanUtil.getBeanByName("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }

    /**
     * 对字符串类型的数据操作
     *
     * @return string类型操作对象
     */
    public static ValueOperations<String, Object> valueOps() {
        return getRedisTemplate().opsForValue();
    }

    /**
     * 对hash类型的数据操作
     *
     * @return hash类型操作对象
     */
    public static HashOperations<String, String, Object> hashOps() {
        return getRedisTemplate().opsForHash();
    }

    /**
     * 对列表类型的数据操作
     *
     * @return list类型操作对象
     */
    public static ListOperations<String, Object> listOps() {
        return getRedisTemplate().opsForList();
    }

    /**
     * 对集合类型的数据操作
     *
     * @return set类型操作对象
     */
    public static SetOperations<String, Object> setOps() {
        return getRedisTemplate().opsForSet();
    }

    /**
     * 对有序集合类型的数据操作
     *
     * @return zset类型操作对象
     */
    public static ZSetOperations<String, Object> zSetOps() {
        return getRedisTemplate().opsForZSet();
    }

    /**
     * 删除指定键值数据
     *
     * @param key 键值
     */
    public static void delete(String key) {
        getRedisTemplate().delete(key);
    }

    /**
     * 批量删除指定键值数据
     *
     * @param keys 键值列表
     */
    public static void delete(List<String> keys) {
        getRedisTemplate().delete(keys);
    }

    /**
     * 指定键值超时时间
     *
     * @param key        键值
     * @param expireTime 超时时间
     */
    public static void expire(String key, Duration expireTime) {
        getRedisTemplate().expire(key, expireTime.toMillis(), TimeUnit.MILLISECONDS);
    }
}