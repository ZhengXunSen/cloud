package com.zxs.cloud.spark.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by bill.zheng in 2018/9/4
 */
public final class JsonUtils {

    private static class JacksonHolder {
        private static ObjectMapper instance = new ObjectMapper();
        protected static ObjectMapper nonInstance = new ObjectMapper();

        static {
            instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            instance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            nonInstance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            nonInstance.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        }
    }

    public static ObjectMapper getInstance() {
        return JacksonHolder.instance;
    }

    private JsonUtils() {
    }

    /**
     * 序列化包含空值
     */
    public static <O> String toJsonIncludeNonProperty(O o) {
        if (o == null)
            return null;
        try {
            return JacksonHolder.nonInstance.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败", e);
        }
    }

    public static <O> String json(O o) {
        if (o == null)
            return null;
        try {
            return getInstance().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("json序列化失败", e);
        }
    }

    public static <T> T parse(String json, Class<T> clazz) {
        if (json == null)
            return null;
        try {
            return getInstance().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException("json反列化失败", e);
        }
    }

    public static <T> T parse(String json, JavaType valueType) {
        if (json == null)
            return null;
        try {
            return getInstance().readValue(json, valueType);
        } catch (IOException e) {
            throw new RuntimeException("json反列化失败", e);
        }
    }

    public static <T> T parse(String json, TypeReference valueTypeRef) {
        if (json == null)
            return null;
        try {
            return getInstance().readValue(json, valueTypeRef);
        } catch (IOException e) {
            throw new RuntimeException("json反列化失败", e);
        }
    }

    public static JavaType constructParametricType(Class<?> parametrized, Class... parameterClasses) {
        return getInstance().getTypeFactory().constructParametricType(parametrized, parameterClasses);
    }
}
