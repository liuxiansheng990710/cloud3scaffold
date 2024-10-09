package com.example.commons.core.factory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import com.example.commons.core.model.Pages;

/**
 * <p>
 * 日志转换工厂类
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 11:45
 */

@Component
public class PageConverterFactory {

    private final Map<Class<?>, Pages.PageConverter<?, ?>> converters = new HashMap<>();

    @Autowired
    public PageConverterFactory(List<Pages.PageConverter<?, ?>> convertersList) {
        // 根据分页的源类型，存储各实现类
        for (Pages.PageConverter<?, ?> converter : convertersList) {
            ResolvableType resolvableType = ResolvableType.forClass(converter.getClass());
            Class<?> sourceClass = resolvableType.as(Pages.PageConverter.class).getGeneric(0).resolve();
            // 将源类型和对应的converter存储到Map中
            converters.put(sourceClass, converter);
        }
    }

    /**
     * 根据源分页类型，获取对应的分页转换工厂方法
     *
     * @param sourceClass 源分页类型
     * @return
     */
    public <S, T> Pages.PageConverter<S, T> getConverter(Class<S> sourceClass) {
        Pages.PageConverter<S, T> pageConverter = (Pages.PageConverter<S, T>) converters.get(sourceClass);
        if (Objects.nonNull(pageConverter)) {
            return pageConverter;
        }
        //存在特例：mongo的Page，传进来的class类型是Page的实现类
        return converters.keySet()
                .stream()
                //获取实现类
                .filter(key -> key.isAssignableFrom(sourceClass))
                .map(e -> (Pages.PageConverter<S, T>) converters.get(e))
                .filter(Objects::nonNull).findFirst().orElse(null);
    }

}

