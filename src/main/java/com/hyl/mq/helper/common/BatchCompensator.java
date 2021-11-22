package com.hyl.mq.helper.common;

import java.util.List;

/**
 * @author huayuanlin
 * @date 2021/11/19 15:14
 * @desc the interface desc
 */
public interface BatchCompensator<T> extends Compensator<T> {


    /**
     * get compensation log items
     *
     * @param batchSize batchSize
     * @return items
     */
    List<T> getItems(int batchSize);

    /**
     * process items
     *
     * @param items items
     */
    void process(List<T> items);

    /**
     * batch size
     *
     * @return batch size
     */
    int batchSize();


    /**
     * doCompensate
     */
    @Override
    default void doCompensate() {
        process(getItems(batchSize()));
    }
}
