package com.example.quartz.provider.stater.convert;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import com.example.commons.core.utils.TypeUtils;
import com.example.quartz.provider.stater.enums.QuartzJobEnum;
import com.example.quartz.provider.stater.model.QuartzJob;

@Mapper(imports = {TypeUtils.class}, typeConversionPolicy = ReportingPolicy.WARN, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface QuartzJobStructConvert {

    QuartzJobStructConvert INSTANCE = Mappers.getMapper(QuartzJobStructConvert.class);

    QuartzJob convertQuartz(QuartzJobEnum quartzJobEnum);

}
