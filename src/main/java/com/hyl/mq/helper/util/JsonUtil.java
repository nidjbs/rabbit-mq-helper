package com.hyl.mq.helper.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import java.io.IOException;
import java.util.List;

/**
 * @author ： huayuanlin
 * @date ： 2021/5/23 16:59
 * @desc ： jackson json util
 */
public class JsonUtil {


    private JsonUtil() {
        throw new UnsupportedOperationException();
    }

    private static final ObjectMapper OBJECT_MAPPER;
    private static final ObjectWriter OBJECT_WRITER;
    private static final ObjectReader OBJECT_READER;


    static {
        ObjectMapper objectMapperIgnoreUnknownProps = new ObjectMapper();
        objectMapperIgnoreUnknownProps.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_WRITER = OBJECT_MAPPER.writer();
        OBJECT_READER = OBJECT_MAPPER.reader();
    }
    /**
     * obj to json
     *
     * @param object obj
     * @return json
     */
    public static String toJsonString(Object object) {
        try {
            return OBJECT_WRITER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * convert to obj
     *
     * @param json  json
     * @param clazz target Class
     * @param <T>   target
     * @return target
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_READER.forType(clazz).readValue(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * json to target array
     *
     * @param json  json
     * @param clazz target class
     * @param <T>   target
     * @return target array
     */
    public static <T> List<T> toArray(String json, Class<T> clazz) {
        try {
            return OBJECT_READER.forType(OBJECT_READER.getTypeFactory().constructCollectionType(List.class, clazz))
                    .readValue(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
