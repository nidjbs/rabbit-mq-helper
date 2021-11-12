package com.hyl.mq.helper.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @author huayuanlin
 * @date 2021/06/04 17:48
 * @desc Non-intrusive builder
 */
public class ObjBuilder<T> {

    private Supplier<T> createInstance;
    
    private List<Consumer<T>> consumers;

    private Predicate<T> predicate;

    private ObjBuilder() {

    }

    /**
     * create builder instance
     *
     * @param constructor build
     * @param <T>         build type
     * @return builder instance
     */
    public static <T> ObjBuilder<T> create(Supplier<T> constructor) {
        ObjBuilder<T> instance = new ObjBuilder<>();
        instance.createInstance = constructor;
        instance.consumers = new ArrayList<>(8);
        return instance;
    }

    /**
     * set obj attributes
     *
     * @param consumer bi consumer
     * @param p        params
     * @param <P>      t
     * @return builder instance
     */
    public <P> ObjBuilder<T> of(BiConsumer<T, P> consumer, P p) {
        consumers.add(instance -> consumer.accept(instance, p));
        return this;
    }

    /**
     * set obj attributes with condition
     *
     * @param consumer bi consumer
     * @param p        params
     * @param <P>      t
     * @return builder instance
     */
    public <P> ObjBuilder<T> of(boolean condition, BiConsumer<T, P> consumer, P p) {
        if (condition) {
            consumers.add(instance -> consumer.accept(instance, p));
        }
        return this;
    }

    /**
     * build pre check
     *
     * @param predicate predicate
     * @return this
     */
    public ObjBuilder<T> preCheck(Predicate<T> predicate) {
        this.predicate =predicate;
        return this;
    }

    /**
     * build obj
     *
     * @return obj
     */
    public T build() {
        T t = createInstance.get();
        for (Consumer<T> consumer : consumers) {
            consumer.accept(t);
        }
        if (predicate != null && !predicate.test(t)) {
            throw new IllegalArgumentException("build obj fail,contains the attribute that the verification fails!");
        }
        return t;
    }

}
