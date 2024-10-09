package com.example.mysql.provider.stater.mp.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.commons.core.model.Pages;
import com.example.commons.core.utils.TypeUtils;

@Mapper(imports = {TypeUtils.class}, typeConversionPolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MyBatisPlusPageStructConvert {

    MyBatisPlusPageStructConvert INSTANCE = Mappers.getMapper(MyBatisPlusPageStructConvert.class);

    @Mapping(target = "current", expression = "java(TypeUtils.castToInt(page.getCurrent()))")
    @Mapping(target = "size", expression = "java(TypeUtils.castToInt(page.getSize()))")
    @Mapping(target = "pages", expression = "java(TypeUtils.castToInt(page.getPages()))")
    @Mapping(target = "total", expression = "java(TypeUtils.castToInt(page.getTotal()))")
    Pages mybatisPlusPageToPagesWithTotal(Page page);

    @Mapping(target = "current", expression = "java(TypeUtils.castToInt(page.getCurrent()))")
    @Mapping(target = "size", expression = "java(TypeUtils.castToInt(page.getSize()))")
    @Mapping(target = "pages", ignore = true)
    @Mapping(target = "total", ignore = true)
    Pages mybatisPlusPageToPages(Page page);

}
