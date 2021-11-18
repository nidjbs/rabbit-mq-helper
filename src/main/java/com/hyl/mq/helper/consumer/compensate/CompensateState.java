package com.hyl.mq.helper.consumer.compensate;

/**
 * @author huayuanlin
 * @date 2021/11/18 15:12
 * @desc the class desc
 */
public enum CompensateState {
    /**
     * Compensate State
     */
    OTHER(-1),
    WAIT_COMPENSATE(1),
    COMPLETE(2);


    private final int id;

    CompensateState(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
