package com.example.commons.core.utils.config.modelmapper.jsr310;

import java.time.temporal.Temporal;

import org.modelmapper.internal.Errors;
import org.modelmapper.spi.ConditionalConverter;
import org.modelmapper.spi.MappingContext;

/**
 * <p>
 * Converts  {@link Temporal} to {@link Temporal}
 * <p>
 *
 * @author : 21
 * @since : 2024/9/24 15:20
 */
public class TemporalToTemporalConverter implements ConditionalConverter<Temporal, Temporal> {

    @Override
    public MatchResult match(Class<?> sourceType, Class<?> destinationType) {
        return Temporal.class.isAssignableFrom(sourceType)
                && Temporal.class.isAssignableFrom(destinationType)
                ? MatchResult.FULL : MatchResult.NONE;
    }

    @Override
    public Temporal convert(MappingContext<Temporal, Temporal> mappingContext) {
        if (mappingContext.getSource() == null) {
            return null;
        } else if (mappingContext.getSourceType().equals(mappingContext.getDestinationType())) {
            return mappingContext.getSource();
        } else {
            throw new Errors().addMessage("Unsupported mapping types[%s->%s]",
                    mappingContext.getSourceType().getName(), mappingContext.getDestinationType())
                    .toMappingException();
        }
    }
}
