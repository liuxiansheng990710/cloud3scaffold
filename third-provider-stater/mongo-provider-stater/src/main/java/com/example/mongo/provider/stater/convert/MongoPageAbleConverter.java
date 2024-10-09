package com.example.mongo.provider.stater.convert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
public class MongoPageAbleConverter<T> implements Pages.PageConverter<Pageable, T> {

    /**
     * 分页转换由Page实现所以这里返回空
     * {@link MongoPageConverter#convert(Page)}
     *
     * @param sourcePage
     * @return
     */
    @Override
    public Pages<T> convert(Pageable sourcePage) {
        return null;
    }

    @Override
    public Pageable defaultPage() {
        return PageRequest.of(0, 20);
    }
}
