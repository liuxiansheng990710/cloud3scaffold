package com.example.commons.web.servlet.model;

import static com.example.commons.web.servlet.enums.WebGlobaErr.x2016;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.example.commons.core.factory.PageConverterFactory;
import com.example.commons.core.model.Pages;
import com.example.commons.core.utils.ApiAssert;
import com.example.commons.core.utils.TypeUtils;

/**
 * <p>
 * controller父类，提供基础方法
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 17:12
 */

public abstract class SuperController {

    @Autowired
    public PageConverterFactory converterFactory;

    /**
     * 对前端传入数据转换（仅针对于基础该类的controller有效）
     * 如需全局配置 @link https://developer.aliyun.com/article/998846#:~:text=%E5%9C%A8SpringMVC
     *
     * @param binder
     */
    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                Long currentTimeMillis = null;
                try {
                    currentTimeMillis = TypeUtils.castToLong(text, null);
                } catch (Exception e) {
                    ApiAssert.failure(x2016, e);
                }
                if (Objects.nonNull(currentTimeMillis)) {
                    // 只解析1970 - 2100 年的时间戳
                    if (currentTimeMillis >= 0L && currentTimeMillis <= 4102416000000L) {
                        setValue(new Date(currentTimeMillis));
                    } else {
                        setValue(null);
                    }
                } else {
                    String format;
                    if (text.length() == 10) {
                        format = "yyyy-MM-dd";
                    } else if (text.length() == "yyyy-MM-dd HH:mm:ss".length()) {
                        format = "yyyy-MM-dd HH:mm:ss";
                    } else {
                        format = "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
                    dateFormat.setTimeZone(TimeZone.getDefault());
                    try {
                        setValue(dateFormat.parse(text));
                    } catch (ParseException e) {
                        setValue(null);
                    }
                }
            }
        });
        // JSONObject 类型转换
        binder.registerCustomEditor(JSONObject.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(JSON.parseObject(text));
            }
        });
        // JSONArray 类型转换
        binder.registerCustomEditor(JSONArray.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(JSON.parseArray(text));
            }
        });
    }

    /**
     * 分页返回（将源分页对象转换为统一分页返回类型）
     * mp使用示例 {@link /sample/mysql/PAGE.md}
     * mongo使用示例 {@link /sample/mongo/PAGE.md}
     *
     * @param sourcePage 源分页对象
     * @param clazz      分页目标类型（显示传递，为了标明 @param T 的类型）
     * @return 分页模型
     */
    protected <S, T> Pages<T> pages(S sourcePage, Class<T> clazz) {
        Pages.PageConverter<S, T> converter = (Pages.PageConverter<S, T>) converterFactory.getConverter(sourcePage.getClass());
        if (Objects.isNull(converter)) {
            return new Pages<>();
        }
        return converter.convert(sourcePage);
    }

    /**
     * 获取默认分页模型
     * mp使用示例 {@link /sample/mysql/PAGE.md}
     * mongo使用示例 {@link /sample/mongo/PAGE.md}
     *
     * @param page 所需分页模型类
     * @return
     */
    protected <S, T> T defaultPage(Class<T> page) {
        Pages.PageConverter<S, T> converter = (Pages.PageConverter<S, T>) converterFactory.getConverter(page);
        return (T) converter.defaultPage();
    }

}
