package com.hyl.mq.helper.cache;

/**
 * @author huayuanlin
 * @date 2021/09/18 15:05
 * @desc the interface desc
 */
public interface Cache<K, V> {

    /**
     * Initialize the cache action, optional
     */
    void initCache();

    /**
     * get from cache
     * @param k key
     * @return result
     */
    V get(K k);

    /**
     * put cache
     *
     * @param k key
     * @param v value
     */
    void put(K k, V v);

    /**
     * clean，optional
     */
    void clean();

    /**
     * remove
     * @param k key
     */
    void remove(K k);
}
