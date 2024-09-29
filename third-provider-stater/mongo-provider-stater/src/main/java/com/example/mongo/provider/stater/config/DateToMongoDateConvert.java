package com.example.mongo.provider.stater.config;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.NonNull;

/**
 * <p>
 * mongo时间类型转换，将ISODate转换为Date类型
 * 主要解决ISODate序列化时，精度缺失问题
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 11:26
 */

public class DateToMongoDateConvert implements Converter<Date, Date> {

    @Override
    public Date convert(@NonNull Date source) {
        String format = DateUtil.format(source, DatePattern.NORM_DATETIME_PATTERN);
        return DateUtil.parse(format);
    }
}
