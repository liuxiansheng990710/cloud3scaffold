package com.example.commons.core.utils;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.commons.core.exceptions.CommonUtilsException;
import com.example.commons.core.utils.config.jackson.deserializer.DateStrDeserializer;
import com.example.commons.core.utils.config.jackson.serializer.DateToNumberSerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cn.hutool.core.date.DatePattern;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JacksonUtils {

    private final static ObjectMapper OBJECT_MAPPER = initObjectMapper(new ObjectMapper());

    /**
     * 获取ObjectMapper
     *
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * 初始化 ObjectMapper
     *
     * @param objectMapper
     * @return
     */
    public static ObjectMapper initObjectMapper(ObjectMapper objectMapper) {
        if (Objects.isNull(objectMapper)) {
            objectMapper = new ObjectMapper();
        }
        return doInitObjectMapper(objectMapper);
    }

    /**
     * 初始化 ObjectMapper 时间方法
     *
     * @return
     */
    private static ObjectMapper doInitObjectMapper(ObjectMapper objectMapper) {
        //因需传递http消息中的mapper 所以暂不处理过时方法
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        objectMapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        objectMapper.disable(MapperFeature.PROPAGATE_TRANSIENT_MARKER);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return registerModule(objectMapper);
    }

    /**
     * 注册模块
     *
     * @param objectMapper
     * @return
     */
    private static ObjectMapper registerModule(ObjectMapper objectMapper) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
        objectMapper.registerModule(new JavaTimeModule());
        //Date 使用自定义序列化
        simpleModule.addSerializer(Date.class, DateToNumberSerializer.instance);
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);
        DateStrDeserializer dateDeserializer = new DateStrDeserializer(DateDeserializers.DateDeserializer.instance, dateFormat, DatePattern.NORM_DATETIME_PATTERN);
        //Date 使用自定义反序列化
        simpleModule.addDeserializer(Date.class, dateDeserializer);
        objectMapper.registerModule(simpleModule);
        return objectMapper;
    }

    /**
     * <p>
     * 是否为CharSequence类型
     * </p>
     *
     * @param object
     * @return
     */
    public static boolean isCharSequence(Object object) {
        return !Objects.isNull(object) && Objects.nonNull(object.getClass()) && CharSequence.class.isAssignableFrom(object.getClass());
    }

    /**
     * 转换Json
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        if (isCharSequence(object)) {
            return (String) object;
        }
        try {
            return getObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new CommonUtilsException(e);
        }
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param json
     * @return
     */
    public static Object parse(String json) {
        Object object = null;
        try {
            object = getObjectMapper().readValue(json, Object.class);
        } catch (Exception ignored) {

        }
        return object;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T readValue(String json, Class<T> clazz) {
        T t = null;
        try {
            t = getObjectMapper().readValue(json, clazz);
        } catch (Exception ignored) {
        }
        return t;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param input
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T readValue(byte[] input, Class<T> clazz) {
        T t = null;
        try {
            t = getObjectMapper().readValue(input, clazz);
        } catch (Exception ignored) {
        }
        return t;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param json
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T readValue(String json, TypeReference<T> valueTypeRef) {
        T t = null;
        try {
            t = getObjectMapper().readValue(json, valueTypeRef);
        } catch (Exception ignored) {
        }
        return t;
    }

    /**
     * Json转换为对象 转换失败返回null
     *
     * @param input
     * @param valueTypeRef
     * @param <T>
     * @return
     */
    public static <T> T readValue(byte[] input, TypeReference<T> valueTypeRef) {
        T t = null;
        try {
            t = getObjectMapper().readValue(input, valueTypeRef);
        } catch (Exception ignored) {
        }
        return t;
    }

    /**
     * 兼容使用fastjson解析json数据(JSONObject)
     *
     * @param json
     * @return
     */
    public static JSONObject parseObject(String json) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(json);
        } catch (Exception ignored) {
        }
        return jsonObject;
    }

    /**
     * 兼容使用fastjson解析json数据(JSONObject)
     *
     * @param obj
     * @return
     */
    public static JSONObject parseObject(Object obj) {
        JSONObject jsonObject = null;
        try {
            jsonObject = JSON.parseObject(toJson(obj));
        } catch (Exception ignored) {
        }
        return jsonObject;
    }

    /**
     * 兼容使用fastjson解析json数据(JSONArray)
     *
     * @param json
     * @return
     */
    public static JSONArray parseArray(String json) {
        JSONArray jsonArray = null;
        try {
            jsonArray = JSON.parseArray(json);
        } catch (Exception ignored) {
        }
        return jsonArray;
    }

    /**
     * 对象转换为实体 转换失败返回null
     *
     * @param obj
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T parse(Object obj, Class<T> clazz) {
        T t = null;
        try {
            t = OBJECT_MAPPER.convertValue(obj, clazz);
        } catch (Exception ignored) {
        }
        return t;
    }

    /**
     * 转换对象为byte
     *
     * @param obj
     * @return
     */
    public static byte[] toBytes(Object obj) {

        byte[] bytes = new byte[0];
        try {
            bytes = getObjectMapper().writeValueAsBytes(obj);
        } catch (Exception ignored) {
        }
        return bytes;
    }

}
