package com.example.mysql.provider.stater.mp.convert;

import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commons.core.model.Pages;

/**
 * <p>
 * mp分页转换实现
 * </p>
 *
 * @author : 21
 * @since : 2024/10/8 10:49
 */

@Component
public class MyBatisPlustPageConverter<T> implements Pages.PageConverter<Page<T>, T> {

    @Override
    public Pages<T> convert(Page<T> page) {
        return page.searchCount() ?
                MyBatisPlusPageStructConvert.INSTANCE.mybatisPlusPageToPagesWithTotal(page) :
                MyBatisPlusPageStructConvert.INSTANCE.mybatisPlusPageToPages(page);
    }

}
