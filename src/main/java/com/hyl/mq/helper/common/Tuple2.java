package com.hyl.mq.helper.common;

/**
 * @author huayuanlin
 * @date 2021/09/26 15:21
 * @desc the class desc
 */
public class Tuple2<M,N> {

    private static final Tuple2 EMPTY = new Tuple2(null,null);

    private final M m;

    private final N n;

    public Tuple2(M m, N n) {
        this.m = m;
        this.n = n;
    }

    @SuppressWarnings("unchecked")
    public static <M,N> Tuple2<M,N> empty() {
        return EMPTY;
    }

    public M left() {
        return m;
    }

    public N right() {
        return n;
    }
}
