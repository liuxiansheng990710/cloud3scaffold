package com.example.commons.core.utils.config.modelmapper.jdk8;

import java.util.Optional;

import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

/**
 * <p>
 * 类型转换 {@link Optional} to {@link Object}
 * </p>
 *
 * @author : 21
 * @since : 2024/9/24 15:22
 */

public class FromOptionalConverter implements ConditionalConverter<Optional<Object>, Object> {

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        //FULL：执行该自定义映射器  NONE：不执行
        return (Optional.class.equals(sourceType) && !Optional.class.equals(destinationType))
                ? MatchResult.FULL
                : MatchResult.NONE;
    }

    @Override
    public Object convert(MappingContext<Optional<Object>, Object> mappingContext) {
        Optional<Object> source = mappingContext.getSource();
        if (source.isEmpty()) {
            return null;
        }
        MappingContext<Object, Object> propertyContext = mappingContext.create(
                source.get(), mappingContext.getDestinationType());
        return mappingContext.getMappingEngine().map(propertyContext);
    }
}
