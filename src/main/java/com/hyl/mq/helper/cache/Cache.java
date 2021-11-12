package com.hyl.mq.helper.cache;

/**
 * @author huayuanlin
 * @date 2021/09/18 15:05
 * @desc the interface desc
 */
public interface Cache<K, V> {

    /**
     * 初始化缓存动作，可选
     */
    void initCache();

    /**
     * 从缓存中获取
     * @param k key
     * @return result
     */
    V get(K k);

    /**
     * 加入缓存中
     *
     * @param k key
     * @param v value
     */
    void put(K k, V v);

    /**
     * 清理缓存，可选
     */
    void clean();

    /**
     * 移除某个缓存
     * @param k key
     */
    void remove(K k);
}
