package com.example.mongo.provider.stater.convert;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import com.example.commons.core.model.Pages;
import com.example.commons.core.utils.TypeUtils;

@Mapper(imports = {TypeUtils.class}, typeConversionPolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MongoPageStructConvert {

    MongoPageStructConvert INSTANCE = Mappers.getMapper(MongoPageStructConvert.class);

    @Mapping(target = "current", expression = "java(TypeUtils.castToInt(page.getNumber()) + 1)")
    @Mapping(target = "size", expression = "java(TypeUtils.castToInt(page.getSize()))")
    @Mapping(target = "pages", expression = "java(TypeUtils.castToInt(page.getTotalPages()))")
    @Mapping(target = "total", expression = "java(TypeUtils.castToInt(page.getTotalElements()))")
    @Mapping(target = "records", source = "content")
    Pages mongoPageToPagesWithTotal(Page page);

}
