package com.hyl.mq.helper.util;


import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author huayuanlin
 * @date 2021/09/27 16:38
 * @desc the class desc
 */
public class StreamUtil {

    private StreamUtil() {
        throw new UnsupportedOperationException();
    }

    /**
     * Stream operations to filter null elements
     */
    public static <T> Stream<T> nonNull(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return Stream.empty();
        }
        return collection.stream().filter(Objects::nonNull);
    }


    /**
     * Stream operations to filter null elements
     */
    public static <T> Stream<T> nonNull(T[] array) {
        if (array == null || array.length <= 0) {
            return Stream.empty();
        }
        return Stream.of(array).filter(Objects::nonNull);
    }

    /**
     * Deduplication operation
     */
    public static <T> List<T> distinct(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        return nonNull(list).distinct().collect(Collectors.toList());
    }

    /**
     * to map
     * Please ensure that the key of the map is unique, otherwise an error will be reported
     */
    public static <K, V, E> Map<K, V> toMap(Collection<E> collection,
                                            Function<E, K> keyFun,
                                            Function<E, V> valueFun) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        Assert.notNull(keyFun,"keyFun is null");
        Assert.notNull(valueFun,"valueFun is null");
        return nonNull(collection).collect(Collectors.toMap(keyFun, valueFun));
    }

    /**
     * to map
     * Please ensure that the key of the map is unique, otherwise an error will be reported
     */
    public static <K, E> Map<K, E> toMap(Collection<E> collection,
                                         Function<E, K> keyFun) {
        if (CollectionUtils.isEmpty(collection)) {
            return Collections.emptyMap();
        }
        return nonNull(collection).collect(Collectors.toMap(keyFun, Function.identity()));
    }


    /**
     * Sort by field (natural sorting by default field)
     *
     * @param list          list ???
     * @param soredFieldFun ??????????????????????????? fun
     * @param isReversed    ????????????
     * @return result
     */
    public static <T, U extends Comparable<? super U>> List<T> sored(List<T> list,
                                                                     Function<T, U> soredFieldFun,
                                                                     boolean isReversed) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        Stream<T> tempStream = nonNull(list);
        Stream<T> sorted;
        if (isReversed) {
            sorted = tempStream.sorted(Comparator.comparing(soredFieldFun).reversed());
        } else {
            sorted = tempStream.sorted(Comparator.comparing(soredFieldFun));
        }
        return sorted.collect(Collectors.toList());
    }


    /**
     * filter
     */
    public static <E> Collection<E> filter(Collection<E> list, Predicate<E> predicate) {
        if (CollectionUtils.isEmpty(list)) {
            return list;
        }
        return nonNull(list).filter(predicate).collect(Collectors.toList());
    }


    /**
     * ???????????????????????????
     */
    public static <R> List<R> slipWithColon(String str, Function<String, R> mapFun) {
        if (StringUtils.isEmpty(str)) {
            return new ArrayList<>();
        }
        return Stream.of(str.split(",")).filter(StringUtils::hasText)
                .map(mapFun).collect(Collectors.toList());
    }
}
