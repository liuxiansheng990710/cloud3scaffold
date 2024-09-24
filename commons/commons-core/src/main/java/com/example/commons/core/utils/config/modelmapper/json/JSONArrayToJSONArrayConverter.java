package com.example.commons.core.utils.config.modelmapper.json;

import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

import com.alibaba.fastjson2.JSONArray;

/**
 * <p>
 * 类型转换  {@link JSONArray} to {@link JSONArray}
 * <P>这里主要是不去操作源对象</P>
 * <p>
 *
 * @author : 21
 * @since : 2024/9/24 15:28
 */
public class JSONArrayToJSONArrayConverter implements ConditionalConverter<JSONArray, JSONArray> {

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return JSONArray.class.isAssignableFrom(sourceType)
                && JSONArray.class.isAssignableFrom(destinationType)
                ? MatchResult.FULL : MatchResult.NONE;
    }

    @Override
    public JSONArray convert(MappingContext<JSONArray, JSONArray> mappingContext) {
        if (mappingContext.getSource() == null) {
            return null;

        } else if (mappingContext.getSourceType().equals(mappingContext.getDestinationType())) {
            JSONArray source = mappingContext.getSource();
            return (JSONArray) source.clone();
        } else {
            throw new Errors().addMessage("Unsupported mapping types[%s->%s]",
                    mappingContext.getSourceType().getName(), mappingContext.getDestinationType())
                    .toMappingException();
        }

    }
}
