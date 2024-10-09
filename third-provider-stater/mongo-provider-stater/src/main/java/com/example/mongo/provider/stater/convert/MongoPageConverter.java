package com.example.mongo.provider.stater.convert;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.example.commons.core.model.Pages;

/**
 * <p>
 * mongo分页转换实现
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 15:15
 */

@Component
public class MongoPageConverter<T> implements Pages.PageConverter<Page<T>, T> {

    @Override
    public Pages<T> convert(Page<T> sourcePage) {
        return MongoPageStructConvert.INSTANCE.mongoPageToPagesWithTotal(sourcePage);
    }
}
